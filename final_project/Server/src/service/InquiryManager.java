package service;

import communication.data.Response;
import communication.data.ResponseStatus;
import repository.*;
import data.*;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class InquiryManager
{
    private static volatile InquiryManager instance;

    private final InquiryRepository dataRepository;
    private final InquiryRepository archiveRepository;

    private final RepresentativeRepository representativeRepository;

    private final Queue<Inquiry> inquiriesQueue = new ConcurrentLinkedQueue<>();

    private final Queue<Representative> existingAgents = new ConcurrentLinkedQueue<>();

    private final Queue<Representative> activeAgents = new ConcurrentLinkedQueue<>();

    private final AtomicInteger nextCodeVal = new AtomicInteger(0);


    public final AtomicInteger currentHandledInquiriesCount = new AtomicInteger(0);


    public static InquiryManager getInstance(
            InquiryRepository dataRepository,
            InquiryRepository archiveRepository,
            RepresentativeRepository repRepo

    ) {
        if (instance == null) {
            synchronized (InquiryManager.class) {
                if (instance == null) {
                    instance = new InquiryManager(
                            dataRepository, archiveRepository,
                            repRepo
                    );
                }
            }
        }
        return instance;
    }

    public static InquiryManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("InquiryManager was not initialized yet.");
        }
        return instance;
    }

    private InquiryManager(
            InquiryRepository dataRepository,
            InquiryRepository archiveRepository,
            RepresentativeRepository repRepo

    )
    {
        this.dataRepository = dataRepository;
        this.archiveRepository = archiveRepository;
        this.representativeRepository = repRepo;


        inquiriesQueue.addAll(dataRepository.readAll());



        existingAgents.addAll(repRepo.readAll());

        startMatchingProcess();

    }


    public synchronized Inquiry addInquiry(Inquiry inquiry){
        if (inquiry == null)
        {
            return null;
        }

        int code = nextCodeVal.getAndIncrement();

        inquiry.setCode(code);

        dataRepository.create(inquiry);


        inquiriesQueue.offer(inquiry);

        return inquiry;
    }

    public List<Inquiry> getAllInquiries(){
        return new ArrayList<>(inquiriesQueue);
    }

    public synchronized boolean cancelInquiry(int code){
        Inquiry target = inquiriesQueue.stream()
                .filter(i -> i.getCode() == code)
                .findFirst()
                .orElse(null);

        if (target == null)
        {
            return false;
        }

        target.setStatus(INQUIRY_STATUS.CANCELED);

        dataRepository.delete(
                target.getCode(),
                target.getType()
        );

        archiveRepository.create(target);

        inquiriesQueue.remove(target);

        return true;
    }

    public int getInquiriesCountByMonth(int month){
        return dataRepository.countByMonth(month)+archiveRepository.countByMonth(month);
    }

    public synchronized Representative addRepresentative( Representative rep ){
        if (rep == null)
        {
            return null;
        }




        existingAgents.offer(rep);

        representativeRepository.saveAll(existingAgents);

        return rep;
    }

    public synchronized boolean deleteRepresentative( int employeeCode ){
        Representative target = null;


        for (Representative rep : existingAgents)
        {
            if (rep.getId() == employeeCode)
            {
                target = rep;
                break;
            }
        }

        if (target == null)
        {
            return false;
        }


        // הסרה מכל הסוכנים
        existingAgents .remove(target);

        // אם פעיל - להסיר גם מהפעילים
        activeAgents.remove(target);

        representativeRepository.saveAll(existingAgents );

        return true;
    }

    public Response getInquiryStatus(String inquiryCode) {

        Inquiry archivedInquiry = archiveRepository.findByCode(inquiryCode);

        if (archivedInquiry != null) {
            return new Response(
                    archivedInquiry.getStatus(),
                    ResponseStatus.SUCCESS,
                    "Inquiry found in archive"
            );
        }

        boolean isPending = inquiriesQueue.stream()
                .anyMatch(i -> i.getCode().toString().equals(inquiryCode));

        if (isPending) {
            return new Response(
                    INQUIRY_STATUS.OPEN,
                    ResponseStatus.SUCCESS,
                    "Inquiry is pending in queue"
            );
        }

        Inquiry activeInquiry = dataRepository.findByCode(inquiryCode);

        if (activeInquiry != null) {
            return new Response(
                    INQUIRY_STATUS.IN_PROGRESS,
                    ResponseStatus.SUCCESS,
                    "Inquiry is currently being handled"
            );
        }

        return new Response(
                null,
                ResponseStatus.FAIL,
                "לא נמצאה פניה עם קוד " + inquiryCode
        );
    }

    public List<Representative> getAllRepresentatives() {
        return new ArrayList<>(existingAgents);
    }

    public boolean loginAgent(int id){
        for (Representative rep : existingAgents )
        {
            if (rep.getId()==id)
            {
                boolean alreadyActive = false;

                for (Representative activeRep : activeAgents)
                {
                    if (activeRep.getId()==id)
                    {
                        alreadyActive = true;
                        break;
                    }
                }

                if (!alreadyActive)
                {
                    activeAgents.offer(rep);
                }

                return true;
            }
        }

        return false;
    }

    public boolean logoutAgent(int id){
        Representative target = null;

        for (Representative rep : activeAgents)
        {
            if (rep.getId()==id)
            {
                target = rep;
                break;
            }
        }

        if (target == null)
        {
            return false;
        }

        activeAgents.remove(target);

        return true;
    }

    public List<Representative> getActiveAgents(){
        return new ArrayList<>(activeAgents);
    }

    private void startMatchingProcess() {
        Thread matchingThread = new Thread(() -> {
        while (true) {

            if (!inquiriesQueue.isEmpty() && !activeAgents.isEmpty()) {

                Inquiry inquiry = inquiriesQueue.poll();
                Representative representative = activeAgents.poll();

                if (inquiry != null && representative != null) {
                    assignInquiryToAgent(inquiry, representative);
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
        matchingThread.setDaemon(true);
        matchingThread.start();
    }


    private void assignInquiryToAgent(Inquiry inquiry,Representative representative) {
        System.out.println("Assigning Inquiry " + inquiry.getCode() + " to Agent " + representative.getFirstName());

        inquiry.setStatus(INQUIRY_STATUS.IN_PROGRESS);

        InquiryAndRepresentative inquiryAndRepresentative = new InquiryAndRepresentative(representative, inquiry);

        currentHandledInquiriesCount.incrementAndGet();

        inquiryAndRepresentative.start();

    }

    // פונקציית עזר שחברה שלך תקרא אליה כשה-Thread שלה מסתיים כדי להחזיר את הסוכן לתור
    public void returnAgentToQueue(Representative representative) {
        if (representative != null) {
            activeAgents.add(representative);
            // הפחתת המונה כי הטיפול בפנייה הסתיים
            currentHandledInquiriesCount.decrementAndGet();
            System.out.println("Rep " + representative.getFirstName() + " returned to queue.");
        }
    }

    public boolean closeInquiry(Inquiry target) {

        target.setStatus(INQUIRY_STATUS.DONE);

        dataRepository.delete(
                target.getCode(),
                target.getType()
        );

        archiveRepository.create(target);

        return true;
    }

}
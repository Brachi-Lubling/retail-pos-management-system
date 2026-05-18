package service;

import repository.*;
import data.*;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class InquiryManager
{
    private final InquiryRepository inquiryRepository;
    private final InquiryRepository archiveRepository;
    private final NextCodeValRepository nextCodeValRepository;

    private final RepresentativeRepository representativeRepository;
    private final RepresentativeCodeRepository representativeCodeRepository;

    private final Queue<Inquiry> inquiriesQueue =
            new ConcurrentLinkedQueue<>();

    private final Queue<Representative> existingAgents =
            new ConcurrentLinkedQueue<>();

    private final Queue<Representative> activeAgents =
            new ConcurrentLinkedQueue<>();

    private final AtomicInteger nextCodeVal =
            new AtomicInteger();

    private final AtomicInteger representativeNextCode =
            new AtomicInteger();

    private final AtomicInteger currentHandledInquiriesCount = new AtomicInteger(0);

    public InquiryManager(
            InquiryRepository inquiryRepository,
            InquiryRepository archiveRepository,
            NextCodeValRepository codeRepo,
            RepresentativeRepository repRepo,
            RepresentativeCodeRepository repCodeRepo
    )
    {
        this.inquiryRepository = inquiryRepository;
        this.archiveRepository = archiveRepository;
        this.nextCodeValRepository = codeRepo;

        this.representativeRepository = repRepo;
        this.representativeCodeRepository = repCodeRepo;

        inquiriesQueue.addAll(inquiryRepository.readAll());
        nextCodeVal.set(codeRepo.get());

        existingAgents.addAll(repRepo.readAll());
        representativeNextCode.set(repCodeRepo.get());

        existingAgents.offer(new Representative("david", "111"));
        existingAgents.offer(new Representative("moshe", "222"));
        existingAgents.offer(new Representative("avi", "333"));

        startMatchingProcess();

    }

    public synchronized Inquiry addInquiry(Inquiry inquiry)
    {
        if (inquiry == null)
        {
            return null;
        }

        int code = nextCodeVal.getAndIncrement();
        inquiry.setCode(code);

        inquiryRepository.create(inquiry);
        nextCodeValRepository.update(nextCodeVal.get());

        inquiriesQueue.offer(inquiry);

        return inquiry;
    }

    public List<Inquiry> getAllInquiries()
    {
        return new ArrayList<>(inquiriesQueue);
    }

    public synchronized boolean cancelInquiry(int code)
    {
        Inquiry target = inquiriesQueue.stream()
                .filter(i -> i.getCode() == code)
                .findFirst()
                .orElse(null);

        if (target == null)
        {
            return false;
        }

        target.setStatus(INQUIRY_STATUS.CANCELED);

        inquiryRepository.delete(target.getCode(), target.getType());
        archiveRepository.create(target);

        inquiriesQueue.remove(target);

        return true;
    }

    public int getInquiriesCountByMonth(int month)
    {
        return inquiryRepository.countByMonth(month);
    }

    public synchronized Representative addRepresentative(Representative rep)
    {
        if (rep == null)
        {
            return null;
        }

        int code = representativeNextCode.getAndIncrement();
        rep.setEmployeeCode(code);

        existingAgents.offer(rep);

        representativeRepository.saveAll(existingAgents);
        representativeCodeRepository.update(representativeNextCode.get());

        return rep;
    }

    public synchronized boolean deleteRepresentative(int employeeCode)
    {
        Representative target = null;

        for (Representative rep : existingAgents)
        {
            if (rep.getEmployeeCode() == employeeCode)
            {
                target = rep;
                break;
            }
        }

        if (target == null)
        {
            return false;
        }

        existingAgents.remove(target);

        representativeRepository.saveAll(existingAgents);

        return true;
    }

    public List<Representative> getAllRepresentatives()
    {
        return new ArrayList<>(existingAgents);
    }

    public boolean loginAgent(String id)
    {
        int size = existingAgents.size();
        boolean found = false;

        for (int i = 0; i < size; i++)
        {
            Representative current = existingAgents.poll();

            if (current != null && current.getId().equals(id))
            {
                activeAgents.offer(current);
                found = true;
            }
            else if (current != null)
            {
                existingAgents.offer(current);
            }
        }

        return found;
    }

    public boolean logoutAgent(String id)
    {
        int size = activeAgents.size();
        boolean found = false;

        for (int i = 0; i < size; i++)
        {
            Representative current = activeAgents.poll();

            if (current != null && current.getId().equals(id))
            {
                existingAgents.offer(current);
                found = true;
            }
            else if (current != null)
            {
                activeAgents.offer(current);
            }
        }

        return found;
    }


    private void startMatchingProcess() { Thread matchingThread = new Thread(() -> {
        while (true) {

            if (!inquiriesQueue.isEmpty() && !activeAgents.isEmpty()) {

                Inquiry inquiry = inquiriesQueue.poll();
                Representative representative = activeAgents.poll();

                if (inquiry != null && representative != null) {
                    assignInquiryToAgent(inquiry, representative);
                }
            }

            try {
                // השהיה קלה כדי לא להעמיס על המעבד
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
        matchingThread.setDaemon(true); // יסגר כשהתוכנית הראשית נסגרת
        matchingThread.start();
    }


    private void assignInquiryToAgent(Inquiry inquiry,Representative representative) {
        System.out.println("Assigning Inquiry " + inquiry.getCode() + " to Agent " + representative.getFirstName());

        inquiry.setStatus(INQUIRY_STATUS.IN_PROGRESS);

        InquiryAndRepresentative inquiryAndRepresentative = new InquiryAndRepresentative();
        inquiryAndRepresentative.setCurrentInquiry(inquiry);
        inquiryAndRepresentative.setCurrentrepresentative(representative);

        if (inquiryRepository != null) {
            inquiryRepository.create(inquiry);
        }

        currentHandledInquiriesCount.incrementAndGet();

        InquiryTreatmentTask treatmentTask = new InquiryTreatmentTask(representative, this);
        treatmentTask.start();
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

    // מתודה המאפשרת למערכת לדעת בכל רגע נתון כמה פניות יש בטיפול (התשובה למרצה)
    public int getNumberOfHandledInquiries() {
        return currentHandledInquiriesCount.get();
    }

}
package service;
import repository.*;
import data.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class InquiryManager {

    private final InquiryRepository dataRepository;
    private final InquiryRepository archiveRepository;
    private final NextCodeValRepository nextCodeValRepository;
    private final Queue<Inquiry> inquiriesQueue = new ConcurrentLinkedQueue<>();
    private final AtomicInteger nextCodeVal = new AtomicInteger();
    private final RepresentativeRepository representativeRepository;
    private final RepresentativeCodeRepository representativeCodeRepository;

    private final Queue<Representative> representativesQueue =
            new ConcurrentLinkedQueue<>();

    private final AtomicInteger representativeNextCode =
            new AtomicInteger();

    public InquiryManager(InquiryRepository dataRepository,
                          InquiryRepository archiveRepository,
                          NextCodeValRepository codeRepo,
                          RepresentativeRepository repRepo,
                          RepresentativeCodeRepository repCodeRepo) {

        this.dataRepository = dataRepository;
        this.archiveRepository = archiveRepository;
        this.nextCodeValRepository = codeRepo;
        this.representativeRepository = repRepo;
        this.representativeCodeRepository = repCodeRepo;

        inquiriesQueue.addAll(dataRepository.readAll());
        nextCodeVal.set(codeRepo.get());

        representativesQueue.addAll(repRepo.readAll());
        representativeNextCode.set(repCodeRepo.get());
    }

    public synchronized Inquiry addInquiry(Inquiry inquiry) {

        if (inquiry == null) return null;

        int code = nextCodeVal.getAndIncrement();
        inquiry.setCode(code);

        dataRepository.create(inquiry);
        nextCodeValRepository.update(nextCodeVal.get());

        inquiriesQueue.offer(inquiry);

        return inquiry;
    }

    public List<Inquiry> getAllInquiries() {
        return new ArrayList<>(inquiriesQueue);
    }

    public synchronized boolean cancelInquiry(int code) {

        Inquiry target = inquiriesQueue.stream()
                .filter(i -> i.getCode() == code)
                .findFirst()
                .orElse(null);

        if (target == null) return false;

        target.setStatus(INQUIRY_STATUS.CANCELED);

        dataRepository.delete(target.getCode(), target.getType());
        archiveRepository.create(target);

        inquiriesQueue.remove(target);
        return true;
    }

    public int getInquiriesCountByMonth(int month)
    {
        return dataRepository.countByMonth(month);
    }
    public synchronized Representative addRepresentative(Representative rep) {

        if (rep == null) return null;

        int code = representativeNextCode.getAndIncrement();
        rep.setEmployeeCode(code);

        representativesQueue.offer(rep);

        representativeRepository.saveAll(representativesQueue);
        representativeCodeRepository.update(representativeNextCode.get());

        return rep;
    }
    public synchronized boolean deleteRepresentative(int employeeCode) {

        Representative target = null;

        for (Representative rep : representativesQueue) {
            if (rep.getEmployeeCode() == employeeCode) {
                target = rep;
                break;
            }
        }

        if (target == null) return false;

        representativesQueue.remove(target);

        representativeRepository.saveAll(representativesQueue);

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

        if (isPendingInQueue) {
            return new Response(
                    "OPEN",
                    ResponseStatus.SUCCESS,
                    "Inquiry is pending in queue"
            );
        }

        Inquiry activeInquiry = dataRepository.findByCode(inquiryCode);

        if (activeInquiry != null) {
            return new Response(
                    "IN_PROGRESS",
                    ResponseStatus.SUCCESS,
                    "Inquiry is currently being handled"
            );
        }

        return new ResponseData(
                ResponseCode.FAIL,
                "לא נמצאה פניה עם קוד " + inquiryCode
        );
    }

    public List<Representative> getAllRepresentatives() {
        return new ArrayList<>(representativesQueue);
    }
}

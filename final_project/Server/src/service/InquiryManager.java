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
    public List<Representative> getAllRepresentatives() {
        return new ArrayList<>(representativesQueue);
    }
}

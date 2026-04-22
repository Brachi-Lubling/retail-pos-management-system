package service;
import repository.*;
import data.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class InquiryManager {

    private final InquiryRepository inquiryRepository;
    private final NextCodeValRepository nextCodeValRepository;
    private final Queue<Inquiry> inquiriesQueue = new ConcurrentLinkedQueue<>();
    private final AtomicInteger nextCodeVal = new AtomicInteger();

    public InquiryManager(InquiryRepository repo, NextCodeValRepository codeRepo) {

        this.inquiryRepository = repo;
        this.nextCodeValRepository = codeRepo;

        inquiriesQueue.addAll(repo.readAll());
        nextCodeVal.set(codeRepo.get());
    }

    public synchronized Inquiry addInquiry(Inquiry inquiry) {

        if (inquiry == null) return null;

        int code = nextCodeVal.getAndIncrement();
        inquiry.setCode(code);

        inquiryRepository.create(inquiry);
        nextCodeValRepository.update(nextCodeVal.get());

        inquiriesQueue.offer(inquiry);

        return inquiry;
    }

    public List<Inquiry> getAllInquiries() {
        return new ArrayList<>(inquiriesQueue);
    }
}

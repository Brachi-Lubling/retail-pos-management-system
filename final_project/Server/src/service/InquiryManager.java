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

    // כל הסוכנים במערכת
    private final Queue<Representative> representativesQueue =
            new ConcurrentLinkedQueue<>();

    // רק הסוכנים הפעילים
    private final Queue<Representative> activeAgents =
            new ConcurrentLinkedQueue<>();

    private final AtomicInteger nextCodeVal =
            new AtomicInteger();

    private final AtomicInteger representativeNextCode =
            new AtomicInteger();

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

        // טעינת פניות
        inquiriesQueue.addAll(inquiryRepository.readAll());

        // טעינת קוד פניה הבא
        nextCodeVal.set(codeRepo.get());

        // טעינת כל הסוכנים מהקובץ
        representativesQueue.addAll(repRepo.readAll());

        // טעינת קוד סוכן הבא
        representativeNextCode.set(repCodeRepo.get());
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

        inquiryRepository.delete(
                target.getCode(),
                target.getType()
        );

        archiveRepository.create(target);

        inquiriesQueue.remove(target);

        return true;
    }

    public int getInquiriesCountByMonth(int month)
    {
        return inquiryRepository.countByMonth(month);
    }

    public synchronized Representative addRepresentative(
            Representative rep
    )
    {
        if (rep == null)
        {
            return null;
        }

        int code = representativeNextCode.getAndIncrement();

        rep.setEmployeeCode(code);

        representativesQueue.offer(rep);

        representativeRepository.saveAll(representativesQueue);

        representativeCodeRepository.update(
                representativeNextCode.get()
        );

        return rep;
    }

    public synchronized boolean deleteRepresentative(
            int employeeCode
    )
    {
        Representative target = null;

        for (Representative rep : representativesQueue)
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

        // הסרה מכל הסוכנים
        representativesQueue.remove(target);

        // אם פעיל - להסיר גם מהפעילים
        activeAgents.remove(target);

        representativeRepository.saveAll(representativesQueue);

        return true;
    }

    public List<Representative> getAllRepresentatives()
    {
        return new ArrayList<>(representativesQueue);
    }

    public boolean loginAgent(String id)
    {
        for (Representative rep : representativesQueue)
        {
            if (rep.getId().equals(id))
            {
                boolean alreadyActive = false;

                for (Representative activeRep : activeAgents)
                {
                    if (activeRep.getId().equals(id))
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

    public boolean logoutAgent(String id)
    {
        Representative target = null;

        for (Representative rep : activeAgents)
        {
            if (rep.getId().equals(id))
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

    public List<Representative> getActiveAgents()
    {
        return new ArrayList<>(activeAgents);
    }
}
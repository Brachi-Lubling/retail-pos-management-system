
package service;

import repository.*;
import data.*;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class InquiryManager
{
    private final InquiryRepository inquiryRepository;
    private final NextCodeValRepository nextCodeValRepository;

    private final Queue<Inquiry> inquiriesQueue =
            new ConcurrentLinkedQueue<>();

    private final Queue<Representative> existingAgents =
            new ConcurrentLinkedQueue<>();

    private final Queue<Representative> activeAgents =
            new ConcurrentLinkedQueue<>();

    private final AtomicInteger nextCodeVal =
            new AtomicInteger();

    public InquiryManager(
            InquiryRepository repo,
            NextCodeValRepository codeRepo
    )
    {
        this.inquiryRepository = repo;
        this.nextCodeValRepository = codeRepo;

        inquiriesQueue.addAll(repo.readAll());
        nextCodeVal.set(codeRepo.get());

        existingAgents.offer(new Representative("david", "111"));
        existingAgents.offer(new Representative("moshe", "222"));
        existingAgents.offer(new Representative("avi", "333"));
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

    public int getInquiriesCountByMonth(int month)
    {
        return inquiryRepository.countByMonth(month);
    }

    public void loginAgent(String id)
    {
        int size = existingAgents.size();

        for (int i = 0; i < size; i++)
        {
            Representative current = existingAgents.poll();

            if (current != null && current.getId().equals(id))
            {
                activeAgents.offer(current);
            }
            else if (current != null)
            {
                existingAgents.offer(current);
            }
        }
    }

    public void logoutAgent(String id)
    {
        int size = activeAgents.size();

        for (int i = 0; i < size; i++)
        {
            Representative current = activeAgents.poll();

            if (current != null && current.getId().equals(id))
            {
                existingAgents.offer(current);
            }
            else if (current != null)
            {
                activeAgents.offer(current);
            }
        }
    }
}
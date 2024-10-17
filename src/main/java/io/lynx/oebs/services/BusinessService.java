package io.lynx.oebs.services;

import io.lynx.oebs.dtos.CreateBusinessRequest;
import io.lynx.oebs.entities.Account;
import io.lynx.oebs.entities.Business;
import io.lynx.oebs.entities.OperatingHours;
import io.lynx.oebs.entities.Tag;
import io.lynx.oebs.exceptions.ResourceBadRequestException;
import io.lynx.oebs.exceptions.ResourceNotFoundException;
import io.lynx.oebs.repositories.AccountRepository;
import io.lynx.oebs.repositories.BusinessRepository;
import io.lynx.oebs.repositories.OperatingHoursRepository;
import io.lynx.oebs.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final TagRepository tagRepository;
    private final OperatingHoursRepository operatingHoursRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public BusinessService(BusinessRepository businessRepository, TagRepository tagRepository, OperatingHoursRepository operatingHoursRepository, AccountRepository accountRepository) {
        this.businessRepository = businessRepository;
        this.tagRepository = tagRepository;
        this.operatingHoursRepository = operatingHoursRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Business createBusiness(CreateBusinessRequest createBusinessRequest, Account account) {
        try {
            if (createBusinessRequest.getTags().isEmpty()) {
                throw new ResourceBadRequestException("one or more tag is required");
            }
            if (createBusinessRequest.getHoursOfOperation().isEmpty()) {
                throw new ResourceBadRequestException("one or more business working hours is required");
            }

            List<Tag> tags = new ArrayList<>();
            for (String tagName : createBusinessRequest.getTags()) {
                Tag tag = tagRepository.findByName(tagName.toLowerCase())
                        .orElseGet(() -> tagRepository.save(new Tag(tagName.toLowerCase()))); // Save if not exists
                tags.add(tag);
            }

            List<OperatingHours> businessHours = createBusinessRequest.getHoursOfOperation().entrySet().stream()
                    .map(entry -> operatingHoursRepository.findByDayAndOpeningTimeAndClosingTime(
                                    DayOfWeek.valueOf(entry.getKey().toUpperCase()), entry.getValue().split("-")[0], entry.getValue().split("-")[1])
                            .orElseThrow(() -> new ResourceNotFoundException("OperatingHours", "day and time", entry.getKey() + ": " + entry.getValue()))
                    ).collect(Collectors.toList());

            Business business = Business.builder()
                    .name(createBusinessRequest.getName()) // Uptown Africa Hair Beauty Salon
                    .description(createBusinessRequest.getDescription()) // Uptown Africa Hair Beauty Salon
                    .email(createBusinessRequest.getEmail()) // support@uptown.com
                    .address(createBusinessRequest.getAddress()) // 8258 118 Ave NW, Edmonton, AB T5B 0S3
                    .phoneNumber(createBusinessRequest.getPhoneNumber()) // 250 783 741 086
                    .owner(account) // owner...
                    .tags(tags) // ["hair", "saloon"]
                    .hoursOfOperation(businessHours) // {"MONDAY":"9AM-6PM","TUESDAY":"9AM-6PM","WEDNESDAY":"9AM-6PM","THURSDAY":"9AM-6PM","FRIDAY":"9AM-6PM","SATURDAY":"10AM-1PM","SUNDAY":"10AM-1PM"}
                    .build();
            return businessRepository.save(business);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Business> getBusinessesByOwner(UUID ownerId) {
        try {
            return businessRepository.findByOwner_AccountId(ownerId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Page<Business> getAllBusinesses(Pageable pageable) {
        try {
            // TODO: if offers aren't loaded
            //  Page<Business> businesses = businessRepository.findAll(pageable);
            //  businesses.getContent().forEach(business -> Hibernate.initialize(business.getOffers()));
            return businessRepository.findAll(pageable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


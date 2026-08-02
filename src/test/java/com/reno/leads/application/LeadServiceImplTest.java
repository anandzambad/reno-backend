package com.reno.leads.application;

import com.reno.leads.api.LeadCreateRequest;
import com.reno.leads.persistence.LeadEntity;
import com.reno.leads.persistence.LeadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeadServiceImplTest {
    @Mock LeadRepository repository;
    @InjectMocks LeadServiceImpl service;

    @Test
    void createMapsAndReturnsLead() {
        when(repository.save(any(LeadEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        var result = service.create(new LeadCreateRequest(1, " Anand ", "ANAND@EXAMPLE.COM", "9876543210", "425001", "Kitchen work"));
        assertThat(result.service()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Anand");
        assertThat(result.email()).isEqualTo("anand@example.com");
        assertThat(result.status()).isEqualTo("NEW");
    }
}

package br.com.vehicle_application.adapters.outbound;

import br.com.vehicle_application.adapters.outbound.db.SaleRepository;
import br.com.vehicle_application.application.mappers.SaleMapper;
import br.com.vehicle_application.core.domain.Sale;
import br.com.vehicle_application.core.entity.SaleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleGatewayTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private SaleMapper saleMapper;

    @InjectMocks
    private SaleGateway saleGateway;

    private Sale sale;
    private SaleEntity saleEntity;

    @BeforeEach
    void setUp() {
        sale = new Sale();
        saleEntity = new SaleEntity();
    }

    @Test
    void saleVehicle_ShouldSaveEntitySuccessfully() {
        // Arrange
        when(saleMapper.toDbEntity(sale)).thenReturn(saleEntity);
        when(saleRepository.save(saleEntity)).thenReturn(saleEntity);

        // Act
        saleGateway.saleVehicle(sale);

        // Assert
        verify(saleMapper, times(1)).toDbEntity(sale);
        verify(saleRepository, times(1)).save(saleEntity);
    }

    @Test
    void saleVehicle_ShouldThrowRuntimeExceptionWhenMapperFails() {
        // Arrange
        when(saleMapper.toDbEntity(sale)).thenThrow(new RuntimeException("Mapping error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> saleGateway.saleVehicle(sale));

        assertEquals("Error when trying to sale vehicle. Please try again later.",
                exception.getMessage());
        verify(saleRepository, never()).save(any());
    }

    @Test
    void saleVehicle_ShouldThrowRuntimeExceptionWhenRepositoryFails() {
        // Arrange
        when(saleMapper.toDbEntity(sale)).thenReturn(saleEntity);
        when(saleRepository.save(saleEntity)).thenThrow(new RuntimeException("DB error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> saleGateway.saleVehicle(sale));

        assertEquals("Error when trying to sale vehicle. Please try again later.",
                exception.getMessage());
    }

    @Test
    void saleVehicle_ShouldUseCorrectMapperAndRepository() {
        // Arrange
        when(saleMapper.toDbEntity(sale)).thenReturn(saleEntity);
        when(saleRepository.save(saleEntity)).thenReturn(saleEntity);

        // Act
        saleGateway.saleVehicle(sale);

        // Assert
        verify(saleMapper).toDbEntity(sale);
        verify(saleRepository).save(saleEntity);
    }
}
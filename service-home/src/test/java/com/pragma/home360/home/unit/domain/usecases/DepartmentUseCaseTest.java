package com.pragma.home360.home.unit.domain.usecases;

import com.pragma.home360.home.domain.exceptions.ValidationException;
import com.pragma.home360.home.domain.model.DepartmentModel;
import com.pragma.home360.home.domain.ports.out.DepartmentPersistencePort;
import com.pragma.home360.home.domain.usecases.DepartmentUseCase;
import com.pragma.home360.home.domain.utils.constants.DomainConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentUseCaseTest {

    @Mock
    private DepartmentPersistencePort departmentPersistencePort;

    @InjectMocks
    private DepartmentUseCase departmentUseCase;

    @Captor
    private ArgumentCaptor<DepartmentModel> departmentModelCaptor;

    private DepartmentModel validDepartment;

    @BeforeEach
    void setUp() {
        validDepartment = new DepartmentModel(1L, "Test Department", "Test Description");
    }

    @Nested
    @DisplayName("Tests para método saveDepartment")
    class SaveDepartmentMethodTests {

        @Test
        @DisplayName("Debería guardar y retornar el departamento cuando es válido")
        void saveDepartment_ValidDepartment_ShouldSaveAndReturnDepartment() {
            // Arrange
            when(departmentPersistencePort.existsDepartmentByName(anyString())).thenReturn(false);
            when(departmentPersistencePort.saveDepartment(any(DepartmentModel.class))).thenReturn(validDepartment);

            // Act
            DepartmentModel result = departmentUseCase.saveDepartment(validDepartment);

            // Assert
            assertNotNull(result);
            assertEquals(validDepartment.getId(), result.getId());
            assertEquals(validDepartment.getName(), result.getName());
            assertEquals(validDepartment.getDescription(), result.getDescription());

            verify(departmentPersistencePort).existsDepartmentByName(validDepartment.getName().trim());
            verify(departmentPersistencePort).saveDepartment(validDepartment);
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el nombre está vacío")
        void saveDepartment_EmptyName_ShouldThrowValidationException() {
            // Arrange
            DepartmentModel invalidDepartment = new DepartmentModel(1L, "", "Test Description");

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> departmentUseCase.saveDepartment(invalidDepartment)
            );

            assertEquals(DomainConstants.DEPARTMENT_NAME_CANNOT_BE_EMPTY, exception.getMessage());
            verify(departmentPersistencePort, never()).existsDepartmentByName(anyString());
            verify(departmentPersistencePort, never()).saveDepartment(any(DepartmentModel.class));
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando la descripción está vacía")
        void saveDepartment_EmptyDescription_ShouldThrowValidationException() {
            // Arrange
            DepartmentModel invalidDepartment = new DepartmentModel(1L, "Test Department", "");

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> departmentUseCase.saveDepartment(invalidDepartment)
            );

            assertEquals(DomainConstants.DEPARTMENT_DESCRIPTION_CANNOT_BE_EMPTY, exception.getMessage());
            verify(departmentPersistencePort, never()).existsDepartmentByName(anyString());
            verify(departmentPersistencePort, never()).saveDepartment(any(DepartmentModel.class));
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el nombre es demasiado largo")
        void saveDepartment_NameTooLong_ShouldThrowValidationException() {
            // Arrange
            String longName = "a".repeat(DomainConstants.DEPARTMENT_NAME_MAX_LENGTH + 1);
            DepartmentModel invalidDepartment = new DepartmentModel(1L, longName, "Test Description");

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> departmentUseCase.saveDepartment(invalidDepartment)
            );

            assertEquals(String.format(DomainConstants.DEPARTMENT_NAME_MAX_LENGTH_EXCEEDED, DomainConstants.DEPARTMENT_NAME_MAX_LENGTH), exception.getMessage());
            verify(departmentPersistencePort, never()).existsDepartmentByName(anyString());
            verify(departmentPersistencePort, never()).saveDepartment(any(DepartmentModel.class));
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando la descripción es demasiado larga")
        void saveDepartment_DescriptionTooLong_ShouldThrowValidationException() {
            // Arrange
            String longDescription = "a".repeat(DomainConstants.DEPARTMENT_DESCRIPTION_MAX_LENGTH + 1);
            DepartmentModel invalidDepartment = new DepartmentModel(1L, "Test Department", longDescription);

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> departmentUseCase.saveDepartment(invalidDepartment)
            );

            assertEquals(String.format(DomainConstants.DEPARTMENT_DESCRIPTION_MAX_LENGTH_EXCEEDED, DomainConstants.DEPARTMENT_DESCRIPTION_MAX_LENGTH), exception.getMessage());
            verify(departmentPersistencePort, never()).existsDepartmentByName(anyString());
            verify(departmentPersistencePort, never()).saveDepartment(any(DepartmentModel.class));
        }

    }

    @Nested
    @DisplayName("Tests para método getDepartmentById")
    class GetDepartmentByIdMethodTests {

        @Test
        @DisplayName("Debería retornar el departamento cuando existe")
        void getDepartmentById_DepartmentExists_ShouldReturnDepartment() {
            // Arrange
            when(departmentPersistencePort.getDepartmentById(any(Long.class))).thenReturn(Optional.of(validDepartment));

            // Act
            Optional<DepartmentModel> result = departmentUseCase.getDepartmentById(validDepartment.getId());

            // Assert
            assertTrue(result.isPresent());
            assertEquals(validDepartment.getId(), result.get().getId());
            assertEquals(validDepartment.getName(), result.get().getName());
            assertEquals(validDepartment.getDescription(), result.get().getDescription());

            verify(departmentPersistencePort).getDepartmentById(validDepartment.getId());
        }

        @Test
        @DisplayName("Debería retornar Optional vacío cuando el departamento no existe")
        void getDepartmentById_DepartmentDoesNotExist_ShouldReturnEmptyOptional() {
            // Arrange
            Long departmentId = 999L;
            when(departmentPersistencePort.getDepartmentById(departmentId)).thenReturn(Optional.empty());

            // Act
            Optional<DepartmentModel> result = departmentUseCase.getDepartmentById(departmentId);

            // Assert
            assertFalse(result.isPresent());
            verify(departmentPersistencePort).getDepartmentById(departmentId);
        }
    }

    @Nested
    @DisplayName("Tests para método getAllDepartments")
    class GetAllDepartmentsMethodTests {

        @Test
        @DisplayName("Debería retornar lista de departamentos")
        void getAllDepartments_ShouldReturnDepartmentList() {
            // Arrange
            int page = 0;
            int size = 10;
            DepartmentModel department1 = new DepartmentModel(1L, "Department 1", "Description 1");
            DepartmentModel department2 = new DepartmentModel(2L, "Department 2", "Description 2");
            List<DepartmentModel> expectedDepartments = Arrays.asList(department1, department2);

            when(departmentPersistencePort.getAllDepartments(page, size)).thenReturn(expectedDepartments);

            // Act
            List<DepartmentModel> result = departmentUseCase.getAllDepartments(page, size);

            // Assert
            assertEquals(expectedDepartments.size(), result.size());
            assertEquals(expectedDepartments, result);
            verify(departmentPersistencePort).getAllDepartments(page, size);
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando page es negativo")
        void getAllDepartments_NegativePage_ShouldThrowValidationException() {
            // Arrange
            int page = -1;
            int size = 10;

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> departmentUseCase.getAllDepartments(page, size)
            );

            assertEquals(DomainConstants.PAGINATION_PAGE_NEGATIVE, exception.getMessage());
            verify(departmentPersistencePort, never()).getAllDepartments(anyInt(), anyInt());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando size es menor que 1")
        void getAllDepartments_SizeTooSmall_ShouldThrowValidationException() {
            // Arrange
            int page = 0;
            int size = 0;

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> departmentUseCase.getAllDepartments(page, size)
            );

            assertEquals(DomainConstants.PAGINATION_SIZE_BETWEEN, exception.getMessage());
            verify(departmentPersistencePort, never()).getAllDepartments(anyInt(), anyInt());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando size es mayor que MAX_PAGE_SIZE")
        void getAllDepartments_SizeTooLarge_ShouldThrowValidationException() {
            // Arrange
            int page = 0;
            int size = DomainConstants.MAX_PAGE_SIZE + 1;

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> departmentUseCase.getAllDepartments(page, size)
            );

            assertEquals(DomainConstants.PAGINATION_SIZE_BETWEEN, exception.getMessage());
            verify(departmentPersistencePort, never()).getAllDepartments(anyInt(), anyInt());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando offset excede el máximo")
        void getAllDepartments_OffsetTooLarge_ShouldThrowValidationException() {
            // Arrange
            int page = 201;  // Con size=50, esto genera un offset mayor a 10,000
            int size = 50;

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> departmentUseCase.getAllDepartments(page, size)
            );

            assertEquals(DomainConstants.PAGINATION_MAX_OFFSET, exception.getMessage());
            verify(departmentPersistencePort, never()).getAllDepartments(anyInt(), anyInt());
        }
    }

    @Nested
    @DisplayName("Tests para método getDepartmentCount")
    class GetDepartmentCountMethodTests {

        @Test
        @DisplayName("Debería retornar el conteo de departamentos")
        void getDepartmentCount_ShouldReturnCount() {
            // Arrange
            long expectedCount = 10L;
            when(departmentPersistencePort.getDepartmentCount()).thenReturn(expectedCount);

            // Act
            long result = departmentUseCase.getDepartmentCount();

            // Assert
            assertEquals(expectedCount, result);
            verify(departmentPersistencePort).getDepartmentCount();
        }
    }

    @Nested
    @DisplayName("Tests para método existsDepartmentByName")
    class ExistsDepartmentByNameMethodTests {

        @Test
        @DisplayName("Debería retornar true cuando el departamento existe")
        void existsDepartmentByName_DepartmentExists_ShouldReturnTrue() {
            // Arrange
            String departmentName = "Existing Department";
            when(departmentPersistencePort.existsDepartmentByName(departmentName)).thenReturn(true);

            // Act
            boolean result = departmentUseCase.existsDepartmentByName(departmentName);

            // Assert
            assertTrue(result);
            verify(departmentPersistencePort).existsDepartmentByName(departmentName);
        }

        @Test
        @DisplayName("Debería retornar false cuando el departamento no existe")
        void existsDepartmentByName_DepartmentDoesNotExist_ShouldReturnFalse() {
            // Arrange
            String departmentName = "Non-existing Department";
            when(departmentPersistencePort.existsDepartmentByName(departmentName)).thenReturn(false);

            // Act
            boolean result = departmentUseCase.existsDepartmentByName(departmentName);

            // Assert
            assertFalse(result);
            verify(departmentPersistencePort).existsDepartmentByName(departmentName);
        }
    }
}
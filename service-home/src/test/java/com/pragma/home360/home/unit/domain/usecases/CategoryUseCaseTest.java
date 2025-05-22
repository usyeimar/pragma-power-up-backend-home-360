package com.pragma.home360.home.unit.domain.usecases;

import com.pragma.home360.home.domain.exceptions.AlreadyExistsException;
import com.pragma.home360.home.domain.exceptions.ValidationException;
import com.pragma.home360.home.domain.model.CategoryFilterModel;
import com.pragma.home360.home.domain.model.CategoryModel;
import com.pragma.home360.home.domain.ports.out.CategoryPersistencePort;
import com.pragma.home360.home.domain.usecases.CategoryUseCase;
import com.pragma.home360.home.domain.utils.constants.DomainConstants;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import com.pragma.home360.home.unit.mocks.CategoryFilterMock;
import com.pragma.home360.home.unit.mocks.CategoryMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryUseCaseTest {

    @Mock
    private CategoryPersistencePort categoryPersistencePort;

    @InjectMocks
    private CategoryUseCase categoryUseCase;

    @Captor
    private ArgumentCaptor<CategoryModel> categoryModelCaptor;

    @Captor
    private ArgumentCaptor<CategoryFilterModel> filterCaptor;

    private CategoryModel validCategory;
    private CategoryFilterModel validFilter;

    @BeforeEach
    void setUp() {
        validCategory = CategoryMock.createDefaultCategoryModel();
        validFilter = CategoryFilterMock.createDefaultCategoryFilter();
    }

    @Nested
    @DisplayName("Tests para método save")
    class SaveMethodTests {

        @Test
        @DisplayName("Debería guardar y retornar la categoría cuando es válida")
        void save_ValidCategory_ShouldSaveAndReturnCategory() {
            // Arrange
            when(categoryPersistencePort.getCategoryByName(anyString())).thenReturn(Optional.empty());
            when(categoryPersistencePort.save(any(CategoryModel.class))).thenReturn(validCategory);

            // Act
            CategoryModel result = categoryUseCase.saveCategory(validCategory);

            // Assert
            assertNotNull(result);
            assertEquals(validCategory.getId(), result.getId());
            assertEquals(validCategory.getName(), result.getName());
            assertEquals(validCategory.getDescription(), result.getDescription());

            verify(categoryPersistencePort).getCategoryByName(validCategory.getName().trim());
            verify(categoryPersistencePort).save(validCategory);
        }

        @Test
        @DisplayName("Debería lanzar AlreadyExistsException cuando la categoría ya existe")
        void save_CategoryAlreadyExists_ShouldThrowAlreadyExistsException() {
            // Arrange
            when(categoryPersistencePort.getCategoryByName(anyString())).thenReturn(Optional.of(validCategory));

            // Act & Assert
            AlreadyExistsException exception = assertThrows(
                    AlreadyExistsException.class,
                    () -> categoryUseCase.saveCategory(validCategory)
            );

            assertEquals(DomainConstants.CATEGORY_ALREADY_EXISTS_EXCEPTION_MESSAGE, exception.getMessage());
            verify(categoryPersistencePort).getCategoryByName(validCategory.getName().trim());
            verify(categoryPersistencePort, never()).save(any(CategoryModel.class));
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el nombre está vacío")
        void save_EmptyName_ShouldThrowValidationException() {
            // Arrange
            CategoryModel invalidCategory = CategoryMock.createCategoryModelWithEmptyName();

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> categoryUseCase.saveCategory(invalidCategory)
            );

            assertEquals(DomainConstants.CATEGORY_NAME_CANNOT_BE_EMPTY, exception.getMessage());
            verify(categoryPersistencePort, never()).getCategoryByName(anyString());
            verify(categoryPersistencePort, never()).save(any(CategoryModel.class));
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando la descripción está vacía")
        void save_EmptyDescription_ShouldThrowValidationException() {
            // Arrange
            CategoryModel invalidCategory = CategoryMock.createCategoryModelWithEmptyDescription();

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> categoryUseCase.saveCategory(invalidCategory)
            );

            assertEquals(DomainConstants.CATEGORY_DESCRIPTION_CANNOT_BE_EMPTY, exception.getMessage());
            verify(categoryPersistencePort, never()).getCategoryByName(anyString());
            verify(categoryPersistencePort, never()).save(any(CategoryModel.class));
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el nombre es demasiado largo")
        void save_NameTooLong_ShouldThrowValidationException() {
            // Arrange
            // Crear un nombre con longitud superior al máximo permitido
            String longName = "a".repeat(DomainConstants.CATEGORY_NAME_MAX_LENGTH + 1);
            CategoryModel invalidCategory = CategoryMock.createCategoryModelWithName(longName);

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> categoryUseCase.saveCategory(invalidCategory)
            );

            assertEquals(DomainConstants.CATEGORY_NAME_MAX_LENGTH_EXCEEDED, exception.getMessage());
            verify(categoryPersistencePort, never()).getCategoryByName(anyString());
            verify(categoryPersistencePort, never()).save(any(CategoryModel.class));
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando la descripción es demasiado larga")
        void save_DescriptionTooLong_ShouldThrowValidationException() {
            // Arrange
            // Crear una descripción con longitud superior al máximo permitido
            String longDescription = "a".repeat(DomainConstants.CATEGORY_DESCRIPTION_MAX_LENGTH + 1);
            CategoryModel invalidCategory = CategoryMock.createCategoryModelWithDescription(longDescription);

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> categoryUseCase.saveCategory(invalidCategory)
            );

            assertEquals(DomainConstants.CATEGORY_DESCRIPTION_MAX_LENGTH_EXCEEDED, exception.getMessage());
            verify(categoryPersistencePort, never()).getCategoryByName(anyString());
            verify(categoryPersistencePort, never()).save(any(CategoryModel.class));
        }
    }

    @Nested
    @DisplayName("Tests para método getCategories")
    class GetCategoriesMethodTests {

        @Test
        @DisplayName("Debería retornar categorías con filtro válido")
        void getCategories_ValidFilter_ShouldReturnCategories() {
            // Arrange
            List<CategoryModel> categoryList = new ArrayList<>();
            categoryList.add(validCategory);

            PagedResult<CategoryModel> expectedResponse = new PagedResult<>(
                    categoryList,
                    validFilter.page(),
                    validFilter.size(),
                    1L,
                    1
            );

            when(categoryPersistencePort.getCategories(any(CategoryFilterModel.class))).thenReturn(expectedResponse);

            // Act
            PagedResult<CategoryModel> result = categoryUseCase.getAllCategories(validFilter);

            // Assert
            assertNotNull(result);
            assertEquals(expectedResponse.content().size(), result.content().size());
            assertEquals(expectedResponse.page(), result.page());
            assertEquals(expectedResponse.size(), result.size());
            assertEquals(expectedResponse.totalElements(), result.totalElements());

            verify(categoryPersistencePort).getCategories(filterCaptor.capture());
            assertEquals(validFilter.page(), filterCaptor.getValue().page());
            assertEquals(validFilter.size(), filterCaptor.getValue().size());
            assertEquals(validFilter.sortField(), filterCaptor.getValue().sortField());
            assertEquals(validFilter.direction(), filterCaptor.getValue().direction());
        }
    }

    @Nested
    @DisplayName("Tests para validación de filtros")
    class FilterValidationTests {

        @Nested
        @DisplayName("Tests para validación de page")
        class PageValidationTests {

            @ParameterizedTest(name = "Con page={0} debe pasar la validación")
            @ValueSource(ints = {0, 1, 10, 100})
            @DisplayName("Debería aceptar números de página válidos")
            void validPage_ShouldPass(int page) {
                // Arrange
                CategoryFilterModel filter = CategoryFilterMock.createCategoryFilterWithPage(page);
                when(categoryPersistencePort.getCategories(any(CategoryFilterModel.class)))
                        .thenReturn(new PagedResult<>(new ArrayList<>(), page, 10, 0, 0));

                // Act & Assert
                assertDoesNotThrow(() -> categoryUseCase.getAllCategories(filter));
                verify(categoryPersistencePort).getCategories(filter);
            }

            @Test
            @DisplayName("Debería lanzar ValidationException con página negativa")
            void negativePage_ShouldThrowValidationException() {
                // Arrange
                CategoryFilterModel invalidFilter = CategoryFilterMock.createCategoryFilterWithNegativePage();

                // Act & Assert
                ValidationException exception = assertThrows(
                        ValidationException.class,
                        () -> categoryUseCase.getAllCategories(invalidFilter)
                );

                assertEquals(DomainConstants.PAGINATION_PAGE_NEGATIVE, exception.getMessage());
                verify(categoryPersistencePort, never()).getCategories(any(CategoryFilterModel.class));
            }
        }

        @Nested
        @DisplayName("Tests para validación de size")
        class SizeValidationTests {

            @ParameterizedTest(name = "Con size={0} debe pasar la validación")
            @ValueSource(ints = {1, 10, 50})
            @DisplayName("Debería aceptar tamaños de página válidos")
            void validSize_ShouldPass(int size) {
                // Arrange
                CategoryFilterModel filter = CategoryFilterMock.createCategoryFilterWithSize(size);
                when(categoryPersistencePort.getCategories(any(CategoryFilterModel.class)))
                        .thenReturn(new PagedResult<>(new ArrayList<>(), 0, size, 0, 0));

                // Act & Assert
                assertDoesNotThrow(() -> categoryUseCase.getAllCategories(filter));
                verify(categoryPersistencePort).getCategories(filter);
            }

            @ParameterizedTest(name = "Con size={0} debe fallar la validación")
            @ValueSource(ints = {0, -1, 51, 100})
            @DisplayName("Debería lanzar ValidationException con tamaño inválido")
            void invalidSize_ShouldThrowValidationException(int size) {
                // Arrange
                CategoryFilterModel invalidFilter = CategoryFilterMock.createCategoryFilterWithInvalidSize(size);

                // Act & Assert
                ValidationException exception = assertThrows(
                        ValidationException.class,
                        () -> categoryUseCase.getAllCategories(invalidFilter)
                );

                assertEquals(DomainConstants.PAGINATION_SIZE_BETWEEN, exception.getMessage());
                verify(categoryPersistencePort, never()).getCategories(any(CategoryFilterModel.class));
            }
        }

        @Nested
        @DisplayName("Tests para validación de máximo offset")
        class MaxOffsetValidationTests {

            @Test
            @DisplayName("Offset que excede el límite debe fallar la validación")
            void exceedingOffset_ShouldThrowValidationException() {
                // Arrange
                CategoryFilterModel invalidFilter = CategoryFilterMock.createCategoryFilterWithExceededOffset();

                // Act & Assert
                ValidationException exception = assertThrows(
                        ValidationException.class,
                        () -> categoryUseCase.getAllCategories(invalidFilter)
                );

                assertEquals(DomainConstants.PAGINATION_MAX_OFFSET, exception.getMessage());
                verify(categoryPersistencePort, never()).getCategories(any(CategoryFilterModel.class));
            }
        }

        @Nested
        @DisplayName("Tests para validación de sortField")
        class SortFieldValidationTests {

            @Test
            @DisplayName("SortField no permitido debe fallar la validación")
            void invalidSortField_ShouldThrowValidationException() {
                // Arrange
                CategoryFilterModel invalidFilter = CategoryFilterMock.createCategoryFilterWithInvalidSortField();

                // Act & Assert
                ValidationException exception = assertThrows(
                        ValidationException.class,
                        () -> categoryUseCase.getAllCategories(invalidFilter)
                );

                assertEquals(DomainConstants.SORT_FIELD_INVALID, exception.getMessage());
                verify(categoryPersistencePort, never()).getCategories(any(CategoryFilterModel.class));
            }
        }
    }
}
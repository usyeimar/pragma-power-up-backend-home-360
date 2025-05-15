package com.pragma.home360.home.mocks;

import com.pragma.home360.home.domain.model.CategoryModel;

/**
 * Clase de utilidad para crear instancias de CategoryModel para pruebas
 */
public class CategoryMock {

    /**
     * Crea un modelo de categoría con los parámetros especificados
     *
     * @param id Identificador de la categoría
     * @param name Nombre de la categoría
     * @param description Descripción de la categoría
     * @return Una instancia de CategoryModel con los valores proporcionados
     */
    public static CategoryModel createCategoryModel(Long id, String name, String description) {
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(id);
        categoryModel.setName(name);
        categoryModel.setDescription(description);
        return categoryModel;
    }

    /**
     * Crea un modelo de categoría con valores predeterminados
     *
     * @return Una instancia de CategoryModel con valores predeterminados
     */
    public static CategoryModel createDefaultCategoryModel() {
        return createCategoryModel(1L, "Categoría Test", "Descripción de categoría para pruebas");
    }

    /**
     * Crea un modelo de categoría con un ID específico
     *
     * @param id Identificador de la categoría
     * @return Una instancia de CategoryModel con el ID especificado
     */
    public static CategoryModel createCategoryModelWithId(Long id) {
        return createCategoryModel(id, "Categoría Test", "Descripción de categoría para pruebas");
    }

    /**
     * Crea un modelo de categoría con un nombre específico
     *
     * @param name Nombre de la categoría
     * @return Una instancia de CategoryModel con el nombre especificado
     */
    public static CategoryModel createCategoryModelWithName(String name) {
        return createCategoryModel(1L, name, "Descripción de categoría para pruebas");
    }

    /**
     * Crea un modelo de categoría con una descripción específica
     *
     * @param description Descripción de la categoría
     * @return Una instancia de CategoryModel con la descripción especificada
     */
    public static CategoryModel createCategoryModelWithDescription(String description) {
        return createCategoryModel(1L, "Categoría Test", description);
    }

    /**
     * Crea una categoría con un nombre vacío (para pruebas de validación)
     *
     * @return Una instancia de CategoryModel con nombre vacío
     */
    public static CategoryModel createCategoryModelWithEmptyName() {
        return createCategoryModel(1L, "", "Descripción de categoría para pruebas");
    }

    /**
     * Crea una categoría con una descripción vacía (para pruebas de validación)
     *
     * @return Una instancia de CategoryModel con descripción vacía
     */
    public static CategoryModel createCategoryModelWithEmptyDescription() {
        return createCategoryModel(1L, "Categoría Test", "");
    }
}
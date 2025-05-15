package com.pragma.home360.home.mocks;

import com.pragma.home360.home.domain.model.DepartmentModel;

/**
 * Clase de utilidad para crear instancias de DepartmentModel para pruebas
 */
public class DepartmentMock {

    /**
     * Crea un modelo de departamento con los parámetros especificados
     *
     * @param id          Identificador del departamento
     * @param name        Nombre del departamento
     * @param description Descripción del departamento
     * @return Una instancia de DepartmentModel con los valores proporcionados
     */
    public static DepartmentModel createDepartmentModel(Long id, String name, String description) {
        DepartmentModel departmentModel = new DepartmentModel();
        departmentModel.setId(id);
        departmentModel.setName(name);
        departmentModel.setDescription(description);
        return departmentModel;
    }

    /**
     * Crea un modelo de departamento con valores predeterminados
     *
     * @return Una instancia de DepartmentModel con valores predeterminados
     */
    public static DepartmentModel createDefaultDepartmentModel() {
        return createDepartmentModel(1L, "Departamento Test", "Descripción de departamento para pruebas");
    }

    /**
     * Crea un modelo de departamento con un ID específico
     *
     * @param id Identificador del departamento
     * @return Una instancia de DepartmentModel con el ID especificado
     */
    public static DepartmentModel createDepartmentModelWithId(Long id) {
        return createDepartmentModel(id, "Departamento Test", "Descripción de departamento para pruebas");
    }

    /**
     * Crea un modelo de departamento con un nombre específico
     *
     * @param name Nombre del departamento
     * @return Una instancia de DepartmentModel con el nombre especificado
     */
    public static DepartmentModel createDepartmentModelWithName(String name) {
        return createDepartmentModel(1L, name, "Descripción de departamento para pruebas");
    }

    /**
     * Crea un modelo de departamento con una descripción específica
     *
     * @param description Descripción del departamento
     * @return Una instancia de DepartmentModel con la descripción especificada
     */
    public static DepartmentModel createDepartmentModelWithDescription(String description) {
        return createDepartmentModel(1L, "Departamento Test", description);
    }

    /**
     * Crea un departamento con un nombre vacío (para pruebas de validación)
     *
     * @return Una instancia de DepartmentModel con nombre vacío
     */
    public static DepartmentModel createDepartmentModelWithEmptyName() {
        return createDepartmentModel(1L, "", "Descripción de departamento para pruebas");
    }

    /**
     * Crea un departamento con una descripción vacía (para pruebas de validación)
     *
     * @return Una instancia de DepartmentModel con descripción vacía
     */
    public static DepartmentModel createDepartmentModelWithEmptyDescription() {
        return createDepartmentModel(1L, "Departamento Test", "");
    }
}
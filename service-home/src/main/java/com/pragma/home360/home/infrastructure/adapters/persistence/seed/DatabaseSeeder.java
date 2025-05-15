package com.pragma.home360.home.infrastructure.adapters.persistence.seed;

import com.pragma.home360.home.infrastructure.entities.*;
import com.pragma.home360.home.infrastructure.repositories.mysql.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final CityRepository cityRepository;
    private final NeighborhoodRepository neighborhoodRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Iniciando la carga de datos iniciales...");

        // Solo cargamos datos si las tablas están vacías
        if (departmentRepository.count() == 0) {
            seedDepartments();
        }

        if (categoryRepository.count() == 0) {
            seedCategories();
        }

        log.info("Carga de datos iniciales completada.");
    }

    private void seedDepartments() {
        log.info("Cargando departamentos...");

        // Crear departamentos
        List<DepartmentEntity> departments = Arrays.asList(createDepartment("Antioquia", "Departamento de Antioquia en Colombia"), createDepartment("Cundinamarca", "Departamento de Cundinamarca en Colombia"), createDepartment("Valle del Cauca", "Departamento del Valle del Cauca en Colombia"), createDepartment("Atlántico", "Departamento del Atlántico en Colombia"), createDepartment("Santander", "Departamento de Santander en Colombia"));

        departments = departmentRepository.saveAll(departments);
        log.info("Se han creado {} departamentos", departments.size());

        // Crear ciudades para los departamentos
        seedCities(departments);
    }

    private void seedCities(List<DepartmentEntity> departments) {
        log.info("Cargando ciudades...");

        DepartmentEntity antioquia = departments.get(0);
        DepartmentEntity cundinamarca = departments.get(1);
        DepartmentEntity valle = departments.get(2);
        DepartmentEntity atlantico = departments.get(3);
        DepartmentEntity santander = departments.get(4);

        List<CityEntity> cities = Arrays.asList(createCity("Medellín", "Capital de Antioquia", antioquia), createCity("Bello", "Municipio al norte de Medellín", antioquia), createCity("Envigado", "Municipio al sur de Medellín", antioquia), createCity("Bogotá", "Capital de Colombia", cundinamarca), createCity("Cali", "Capital del Valle del Cauca", valle), createCity("Barranquilla", "Capital del Atlántico", atlantico), createCity("Bucaramanga", "Capital de Santander", santander));

        cities = cityRepository.saveAll(cities);
        log.info("Se han creado {} ciudades", cities.size());

        // Crear barrios para las ciudades
        seedNeighborhoods(cities);
    }

    private void seedNeighborhoods(List<CityEntity> cities) {
        log.info("Cargando barrios...");

        CityEntity medellin = cities.get(0);
        CityEntity bogota = cities.get(3);
        CityEntity cali = cities.get(4);

        List<NeighborHoodEntity> neighborhoods = Arrays.asList(createNeighborhood("El Poblado", "Barrio exclusivo al sur de Medellín", medellin), createNeighborhood("Laureles", "Barrio tradicional de Medellín", medellin), createNeighborhood("Belén", "Barrio tradicional al suroccidente de Medellín", medellin), createNeighborhood("Chapinero", "Barrio central de Bogotá", bogota), createNeighborhood("Usaquén", "Barrio al norte de Bogotá", bogota), createNeighborhood("Granada", "Barrio exclusivo de Cali", cali));

        neighborhoods = neighborhoodRepository.saveAll(neighborhoods);
        log.info("Se han creado {} barrios", neighborhoods.size());

        // Crear ubicaciones para los barrios
        seedLocations(neighborhoods);
    }

    private void seedLocations(List<NeighborHoodEntity> neighborhoods) {
        log.info("Cargando ubicaciones...");

        NeighborHoodEntity poblado = neighborhoods.get(0);
        NeighborHoodEntity laureles = neighborhoods.get(1);
        NeighborHoodEntity belen = neighborhoods.get(2);
        NeighborHoodEntity chapinero = neighborhoods.get(3);
        NeighborHoodEntity usaquen = neighborhoods.get(4);
        NeighborHoodEntity granada = neighborhoods.get(5);

        List<LocationEntity> locations = Arrays.asList(
                // El Poblado
                createLocation("Calle 10 #43-20", 6.2116, -75.5689, "Centro Comercial Oviedo", poblado),
                createLocation("Calle 10A #43-30", 6.2118, -75.5685, "Centro Comercial Oviedo", poblado),
                createLocation("Calle 10 #43B-22", 6.2119, -75.5691, "Hotel Estelar Milla de Oro", poblado),
                createLocation("Carrera 43A #6 Sur-26", 6.1984, -75.5771, "Centro Comercial Santa Fe", poblado),
                createLocation("Carrera 43A #6 Sur-26", 6.1992, -75.5746, "Clínica Medellín del Poblado", poblado),
                createLocation("Calle 7 #42-70", 6.2121, -75.5732, "Parque Lineal La Presidenta", poblado),
                createLocation("Carrera 34 #5G-54", 6.2079, -75.5654, "Museo El Castillo", poblado),


                // Laureles
                createLocation("Carrera 70 #C1-85", 6.2459, -75.5912, "Estadio Atanasio Girardot", laureles),
                createLocation("Carrera 70A #C2-90", 6.2460, -75.5915, "Estadio Atanasio", laureles),
                createLocation("Circular 75 #38-40", 6.2564, -75.5958, "Parque de Laureles", laureles),
                createLocation("Circular 74B #39-65", 6.2510, -75.5941, "Universidad Pontificia Bolivariana", laureles),
                createLocation("Carrera 76 #33-20", 6.2412, -75.5999, "Primer Parque de Laureles", laureles),
                createLocation("Circular 2 #70-10", 6.2450, -75.5895, "Café Zeppelin", laureles),


                // Belén
                createLocation("Carrera 76 #32B-07", 6.2335, -75.6102, "Unidad Deportiva de Belén", belen),
                createLocation("Carrera 76 #32B-15", 6.2337, -75.6100, "Unidad Deportiva", belen),
                createLocation("Calle 30A #82A-26", 6.2304, -75.6115, "Aeroparque Juan Pablo II", belen),
                createLocation("Carrera 80 #32A-55", 6.2310, -75.6132, "Centro Comercial Los Molinos", belen),
                createLocation("Carrera 76 #27-35", 6.2289, -75.6090, "Hospital General de Medellín - Sede Belén", belen),


                // Chapinero
                createLocation("Carrera 7 #45-72", 4.6295, -74.0627, "Zona T", chapinero),
                createLocation("Carrera 7 #46-80", 4.6302, -74.0620, "Zona Rosa", chapinero),
                createLocation("Calle 57 #9-17", 4.6456, -74.0595, "Universidad Piloto", chapinero),
                createLocation("Calle 45 #6-15", 4.6321, -74.0638, "Universidad Javeriana", chapinero),
                createLocation("Carrera 9 #59-30", 4.6457, -74.0593, "Centro Comercial Andino", chapinero),
                createLocation("Calle 57 #7-29", 4.6380, -74.0625, "Gimnasio Moderno", chapinero),


                // Usaquén
                createLocation("Carrera 7 #120-20", 4.7105, -74.0301, "Centro Comercial Hacienda Santa Bárbara", usaquen),
                createLocation("Carrera 7 #120A-18", 4.7106, -74.0303, "Centro Comercial Hacienda Santa Bárbara", usaquen),
                createLocation("Carrera 6 #117-70", 4.7132, -74.0334, "Usaquén Park", usaquen),
                createLocation("Carrera 7 #116-50", 4.7105, -74.0329, "Hospital Fundación Santa Fe", usaquen),
                createLocation("Calle 119 #6A-48", 4.7135, -74.0331, "Mercado de las Pulgas de Usaquén", usaquen),
                createLocation("Carrera 9 #115-94", 4.7118, -74.0330, "Universidad El Bosque", usaquen),


                // Granada (Cali)
                createLocation("Carrera 66 #6-50", 3.4427, -76.5311, "Parque del Perro", granada),
                createLocation("Carrera 66A #6-48", 3.4429, -76.5312, "Parque del Perro", granada),
                createLocation("Calle 9 #35-28", 3.4408, -76.5325, "Granada Gourmet", granada),
                createLocation("Avenida 6N #23DN-30", 3.4640, -76.5264, "Clínica de Occidente", granada),
                createLocation("Carrera 5N #20-20", 3.4632, -76.5292, "Centro Comercial Centenario", granada),
                createLocation("Calle 21N #4N-50", 3.4648, -76.5283, "Biblioteca Departamental Jorge Garcés Borrero", granada)


        );

        locations = locationRepository.saveAll(locations);
        log.info("Se han creado {} ubicaciones", locations.size());
    }

    private void seedCategories() {
        log.info("Cargando categorías...");

        List<CategoryEntity> categories = Arrays.asList(createCategory("Apartamento", "Unidad de vivienda en edificio residencial"), createCategory("Casa", "Vivienda independiente unifamiliar"), createCategory("Oficina", "Espacio para uso profesional o empresarial"), createCategory("Local Comercial", "Espacio para uso comercial o de negocio"), createCategory("Finca", "Propiedad rural para actividades agrícolas o recreativas"), createCategory("Casa Campestre", "Vivienda con amplias zonas verdes"), createCategory("Lote", "Terreno sin construir para desarrollo"));

        categories = categoryRepository.saveAll(categories);
        log.info("Se han creado {} categorías", categories.size());
    }

    // Métodos de ayuda para crear entidades

    private DepartmentEntity createDepartment(String name, String description) {
        DepartmentEntity department = new DepartmentEntity();
        department.setName(name);
        department.setDescription(description);
        return department;
    }

    private CityEntity createCity(String name, String description, DepartmentEntity department) {
        CityEntity city = new CityEntity();
        city.setName(name);
        city.setDescription(description);
        city.setDepartment(department);
        return city;
    }

    private NeighborHoodEntity createNeighborhood(String name, String description, CityEntity city) {
        NeighborHoodEntity neighborhood = new NeighborHoodEntity();
        neighborhood.setName(name);
        neighborhood.setDescription(description);
        neighborhood.setCity(city);
        return neighborhood;
    }

    private LocationEntity createLocation(String address, Double latitude, Double longitude, String referencePoint, NeighborHoodEntity neighborhood) {
        LocationEntity location = new LocationEntity();
        location.setAddress(address);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setReferencePoint(referencePoint);
        location.setNeighborhood(neighborhood);
        return location;
    }

    private CategoryEntity createCategory(String name, String description) {
        CategoryEntity category = new CategoryEntity();
        category.setName(name);
        category.setDescription(description);
        return category;
    }
}
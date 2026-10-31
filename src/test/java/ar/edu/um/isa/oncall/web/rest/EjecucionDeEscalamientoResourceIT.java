package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.EjecucionDeEscalamientoAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.EjecucionDeEscalamiento;
import ar.edu.um.isa.oncall.domain.Incidente;
import ar.edu.um.isa.oncall.domain.enumeration.ResultadoEscalamiento;
import ar.edu.um.isa.oncall.repository.EjecucionDeEscalamientoRepository;
import ar.edu.um.isa.oncall.repository.UserRepository;
import ar.edu.um.isa.oncall.service.EjecucionDeEscalamientoService;
import ar.edu.um.isa.oncall.service.mapper.EjecucionDeEscalamientoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EjecucionDeEscalamientoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EjecucionDeEscalamientoResourceIT {

    private static final Integer DEFAULT_VUELTA = 0;
    private static final Integer UPDATED_VUELTA = 1;

    private static final Integer DEFAULT_NIVEL = 0;
    private static final Integer UPDATED_NIVEL = 1;

    private static final ResultadoEscalamiento DEFAULT_RESULTADO = ResultadoEscalamiento.NOTIFICADO;
    private static final ResultadoEscalamiento UPDATED_RESULTADO = ResultadoEscalamiento.SIN_DESTINATARIO;

    private static final Instant DEFAULT_EJECUTADO_EN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EJECUTADO_EN = Instant.ofEpochMilli(1701729143509L);

    private static final Instant DEFAULT_PROXIMO_EN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROXIMO_EN = Instant.ofEpochMilli(1701729143509L);

    private static final String DEFAULT_DETALLE = "AAAAAAAAAA";
    private static final String UPDATED_DETALLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ejecucion-de-escalamientos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EjecucionDeEscalamientoRepository ejecucionDeEscalamientoRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private EjecucionDeEscalamientoRepository ejecucionDeEscalamientoRepositoryMock;

    @Autowired
    private EjecucionDeEscalamientoMapper ejecucionDeEscalamientoMapper;

    @Mock
    private EjecucionDeEscalamientoService ejecucionDeEscalamientoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEjecucionDeEscalamientoMockMvc;

    private EjecucionDeEscalamiento ejecucionDeEscalamiento;

    private EjecucionDeEscalamiento insertedEjecucionDeEscalamiento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EjecucionDeEscalamiento createEntity(EntityManager em) {
        EjecucionDeEscalamiento ejecucionDeEscalamiento = new EjecucionDeEscalamiento()
            .vuelta(DEFAULT_VUELTA)
            .nivel(DEFAULT_NIVEL)
            .resultado(DEFAULT_RESULTADO)
            .ejecutadoEn(DEFAULT_EJECUTADO_EN)
            .proximoEn(DEFAULT_PROXIMO_EN)
            .detalle(DEFAULT_DETALLE);
        // Add required entity
        Incidente incidente;
        if (TestUtil.findAll(em, Incidente.class).isEmpty()) {
            incidente = IncidenteResourceIT.createEntity();
            em.persist(incidente);
            em.flush();
        } else {
            incidente = TestUtil.findAll(em, Incidente.class).get(0);
        }
        ejecucionDeEscalamiento.setIncidente(incidente);
        return ejecucionDeEscalamiento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EjecucionDeEscalamiento createUpdatedEntity(EntityManager em) {
        EjecucionDeEscalamiento updatedEjecucionDeEscalamiento = new EjecucionDeEscalamiento()
            .vuelta(UPDATED_VUELTA)
            .nivel(UPDATED_NIVEL)
            .resultado(UPDATED_RESULTADO)
            .ejecutadoEn(UPDATED_EJECUTADO_EN)
            .proximoEn(UPDATED_PROXIMO_EN)
            .detalle(UPDATED_DETALLE);
        // Add required entity
        Incidente incidente;
        if (TestUtil.findAll(em, Incidente.class).isEmpty()) {
            incidente = IncidenteResourceIT.createUpdatedEntity();
            em.persist(incidente);
            em.flush();
        } else {
            incidente = TestUtil.findAll(em, Incidente.class).get(0);
        }
        updatedEjecucionDeEscalamiento.setIncidente(incidente);
        return updatedEjecucionDeEscalamiento;
    }

    @BeforeEach
    void initTest() {
        ejecucionDeEscalamiento = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEjecucionDeEscalamiento != null) {
            ejecucionDeEscalamientoRepository.delete(insertedEjecucionDeEscalamiento);
            insertedEjecucionDeEscalamiento = null;
        }
    }

    @Test
    @Transactional
    void getAllEjecucionDeEscalamientos() throws Exception {
        // Initialize the database
        insertedEjecucionDeEscalamiento = ejecucionDeEscalamientoRepository.saveAndFlush(ejecucionDeEscalamiento);

        // Get all the ejecucionDeEscalamientoList
        restEjecucionDeEscalamientoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ejecucionDeEscalamiento.getId().intValue())))
            .andExpect(jsonPath("$.[*].vuelta").value(hasItem(DEFAULT_VUELTA)))
            .andExpect(jsonPath("$.[*].nivel").value(hasItem(DEFAULT_NIVEL)))
            .andExpect(jsonPath("$.[*].resultado").value(hasItem(DEFAULT_RESULTADO.toString())))
            .andExpect(jsonPath("$.[*].ejecutadoEn").value(hasItem(DEFAULT_EJECUTADO_EN.toString())))
            .andExpect(jsonPath("$.[*].proximoEn").value(hasItem(DEFAULT_PROXIMO_EN.toString())))
            .andExpect(jsonPath("$.[*].detalle").value(hasItem(DEFAULT_DETALLE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEjecucionDeEscalamientosWithEagerRelationshipsIsEnabled() throws Exception {
        when(ejecucionDeEscalamientoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEjecucionDeEscalamientoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ejecucionDeEscalamientoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEjecucionDeEscalamientosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ejecucionDeEscalamientoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEjecucionDeEscalamientoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ejecucionDeEscalamientoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEjecucionDeEscalamiento() throws Exception {
        // Initialize the database
        insertedEjecucionDeEscalamiento = ejecucionDeEscalamientoRepository.saveAndFlush(ejecucionDeEscalamiento);

        // Get the ejecucionDeEscalamiento
        restEjecucionDeEscalamientoMockMvc
            .perform(get(ENTITY_API_URL_ID, ejecucionDeEscalamiento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ejecucionDeEscalamiento.getId().intValue()))
            .andExpect(jsonPath("$.vuelta").value(DEFAULT_VUELTA))
            .andExpect(jsonPath("$.nivel").value(DEFAULT_NIVEL))
            .andExpect(jsonPath("$.resultado").value(DEFAULT_RESULTADO.toString()))
            .andExpect(jsonPath("$.ejecutadoEn").value(DEFAULT_EJECUTADO_EN.toString()))
            .andExpect(jsonPath("$.proximoEn").value(DEFAULT_PROXIMO_EN.toString()))
            .andExpect(jsonPath("$.detalle").value(DEFAULT_DETALLE));
    }

    @Test
    @Transactional
    void getNonExistingEjecucionDeEscalamiento() throws Exception {
        // Get the ejecucionDeEscalamiento
        restEjecucionDeEscalamientoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    protected long getRepositoryCount() {
        return ejecucionDeEscalamientoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected EjecucionDeEscalamiento getPersistedEjecucionDeEscalamiento(EjecucionDeEscalamiento ejecucionDeEscalamiento) {
        return ejecucionDeEscalamientoRepository.findById(ejecucionDeEscalamiento.getId()).orElseThrow();
    }

    protected void assertPersistedEjecucionDeEscalamientoToMatchAllProperties(EjecucionDeEscalamiento expectedEjecucionDeEscalamiento) {
        assertEjecucionDeEscalamientoAllPropertiesEquals(
            expectedEjecucionDeEscalamiento,
            getPersistedEjecucionDeEscalamiento(expectedEjecucionDeEscalamiento)
        );
    }

    protected void assertPersistedEjecucionDeEscalamientoToMatchUpdatableProperties(
        EjecucionDeEscalamiento expectedEjecucionDeEscalamiento
    ) {
        assertEjecucionDeEscalamientoAllUpdatablePropertiesEquals(
            expectedEjecucionDeEscalamiento,
            getPersistedEjecucionDeEscalamiento(expectedEjecucionDeEscalamiento)
        );
    }
}

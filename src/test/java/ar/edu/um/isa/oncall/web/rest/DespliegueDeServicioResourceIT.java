package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.DespliegueDeServicioAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.Cambio;
import ar.edu.um.isa.oncall.domain.DespliegueDeServicio;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.repository.DespliegueDeServicioRepository;
import ar.edu.um.isa.oncall.service.DespliegueDeServicioService;
import ar.edu.um.isa.oncall.service.dto.DespliegueDeServicioDTO;
import ar.edu.um.isa.oncall.service.mapper.DespliegueDeServicioMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link DespliegueDeServicioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DespliegueDeServicioResourceIT {

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DESPLEGADO_EN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DESPLEGADO_EN = Instant.ofEpochMilli(1701729143509L);

    private static final String ENTITY_API_URL = "/api/despliegue-de-servicios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DespliegueDeServicioRepository despliegueDeServicioRepository;

    @Mock
    private DespliegueDeServicioRepository despliegueDeServicioRepositoryMock;

    @Autowired
    private DespliegueDeServicioMapper despliegueDeServicioMapper;

    @Mock
    private DespliegueDeServicioService despliegueDeServicioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDespliegueDeServicioMockMvc;

    private DespliegueDeServicio despliegueDeServicio;

    private DespliegueDeServicio insertedDespliegueDeServicio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DespliegueDeServicio createEntity(EntityManager em) {
        DespliegueDeServicio despliegueDeServicio = new DespliegueDeServicio().version(DEFAULT_VERSION).desplegadoEn(DEFAULT_DESPLEGADO_EN);
        // Add required entity
        Cambio cambio;
        if (TestUtil.findAll(em, Cambio.class).isEmpty()) {
            cambio = CambioResourceIT.createEntity(em);
            em.persist(cambio);
            em.flush();
        } else {
            cambio = TestUtil.findAll(em, Cambio.class).get(0);
        }
        despliegueDeServicio.setCambio(cambio);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        despliegueDeServicio.setServicio(servicio);
        return despliegueDeServicio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DespliegueDeServicio createUpdatedEntity(EntityManager em) {
        DespliegueDeServicio updatedDespliegueDeServicio = new DespliegueDeServicio()
            .version(UPDATED_VERSION)
            .desplegadoEn(UPDATED_DESPLEGADO_EN);
        // Add required entity
        Cambio cambio;
        if (TestUtil.findAll(em, Cambio.class).isEmpty()) {
            cambio = CambioResourceIT.createUpdatedEntity(em);
            em.persist(cambio);
            em.flush();
        } else {
            cambio = TestUtil.findAll(em, Cambio.class).get(0);
        }
        updatedDespliegueDeServicio.setCambio(cambio);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createUpdatedEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        updatedDespliegueDeServicio.setServicio(servicio);
        return updatedDespliegueDeServicio;
    }

    @BeforeEach
    void initTest() {
        despliegueDeServicio = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDespliegueDeServicio != null) {
            despliegueDeServicioRepository.delete(insertedDespliegueDeServicio);
            insertedDespliegueDeServicio = null;
        }
    }

    @Test
    @Transactional
    void createDespliegueDeServicio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DespliegueDeServicio
        DespliegueDeServicioDTO despliegueDeServicioDTO = despliegueDeServicioMapper.toDto(despliegueDeServicio);
        var returnedDespliegueDeServicioDTO = om.readValue(
            restDespliegueDeServicioMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(despliegueDeServicioDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DespliegueDeServicioDTO.class
        );

        // Validate the DespliegueDeServicio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDespliegueDeServicio = despliegueDeServicioMapper.toEntity(returnedDespliegueDeServicioDTO);
        assertDespliegueDeServicioUpdatableFieldsEquals(
            returnedDespliegueDeServicio,
            getPersistedDespliegueDeServicio(returnedDespliegueDeServicio)
        );

        insertedDespliegueDeServicio = returnedDespliegueDeServicio;
    }

    @Test
    @Transactional
    void createDespliegueDeServicioWithExistingId() throws Exception {
        // Create the DespliegueDeServicio with an existing ID
        despliegueDeServicio.setId(1L);
        DespliegueDeServicioDTO despliegueDeServicioDTO = despliegueDeServicioMapper.toDto(despliegueDeServicio);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDespliegueDeServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(despliegueDeServicioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DespliegueDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        despliegueDeServicio.setVersion(null);

        // Create the DespliegueDeServicio, which fails.
        DespliegueDeServicioDTO despliegueDeServicioDTO = despliegueDeServicioMapper.toDto(despliegueDeServicio);

        restDespliegueDeServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(despliegueDeServicioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDesplegadoEnIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        despliegueDeServicio.setDesplegadoEn(null);

        // Create the DespliegueDeServicio, which fails.
        DespliegueDeServicioDTO despliegueDeServicioDTO = despliegueDeServicioMapper.toDto(despliegueDeServicio);

        restDespliegueDeServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(despliegueDeServicioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDespliegueDeServicios() throws Exception {
        // Initialize the database
        insertedDespliegueDeServicio = despliegueDeServicioRepository.saveAndFlush(despliegueDeServicio);

        // Get all the despliegueDeServicioList
        restDespliegueDeServicioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(despliegueDeServicio.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].desplegadoEn").value(hasItem(DEFAULT_DESPLEGADO_EN.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDespliegueDeServiciosWithEagerRelationshipsIsEnabled() throws Exception {
        when(despliegueDeServicioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDespliegueDeServicioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(despliegueDeServicioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDespliegueDeServiciosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(despliegueDeServicioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDespliegueDeServicioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(despliegueDeServicioRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDespliegueDeServicio() throws Exception {
        // Initialize the database
        insertedDespliegueDeServicio = despliegueDeServicioRepository.saveAndFlush(despliegueDeServicio);

        // Get the despliegueDeServicio
        restDespliegueDeServicioMockMvc
            .perform(get(ENTITY_API_URL_ID, despliegueDeServicio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(despliegueDeServicio.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.desplegadoEn").value(DEFAULT_DESPLEGADO_EN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDespliegueDeServicio() throws Exception {
        // Get the despliegueDeServicio
        restDespliegueDeServicioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDespliegueDeServicio() throws Exception {
        // Initialize the database
        insertedDespliegueDeServicio = despliegueDeServicioRepository.saveAndFlush(despliegueDeServicio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the despliegueDeServicio
        DespliegueDeServicio updatedDespliegueDeServicio = despliegueDeServicioRepository
            .findById(despliegueDeServicio.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedDespliegueDeServicio are not directly saved in db
        em.detach(updatedDespliegueDeServicio);
        updatedDespliegueDeServicio.version(UPDATED_VERSION).desplegadoEn(UPDATED_DESPLEGADO_EN);
        DespliegueDeServicioDTO despliegueDeServicioDTO = despliegueDeServicioMapper.toDto(updatedDespliegueDeServicio);

        restDespliegueDeServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, despliegueDeServicioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(despliegueDeServicioDTO))
            )
            .andExpect(status().isOk());

        // Validate the DespliegueDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDespliegueDeServicioToMatchAllProperties(updatedDespliegueDeServicio);
    }

    @Test
    @Transactional
    void putNonExistingDespliegueDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        despliegueDeServicio.setId(longCount.incrementAndGet());

        // Create the DespliegueDeServicio
        DespliegueDeServicioDTO despliegueDeServicioDTO = despliegueDeServicioMapper.toDto(despliegueDeServicio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDespliegueDeServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, despliegueDeServicioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(despliegueDeServicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DespliegueDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDespliegueDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        despliegueDeServicio.setId(longCount.incrementAndGet());

        // Create the DespliegueDeServicio
        DespliegueDeServicioDTO despliegueDeServicioDTO = despliegueDeServicioMapper.toDto(despliegueDeServicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDespliegueDeServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(despliegueDeServicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DespliegueDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDespliegueDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        despliegueDeServicio.setId(longCount.incrementAndGet());

        // Create the DespliegueDeServicio
        DespliegueDeServicioDTO despliegueDeServicioDTO = despliegueDeServicioMapper.toDto(despliegueDeServicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDespliegueDeServicioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(despliegueDeServicioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DespliegueDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDespliegueDeServicioWithPatch() throws Exception {
        // Initialize the database
        insertedDespliegueDeServicio = despliegueDeServicioRepository.saveAndFlush(despliegueDeServicio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the despliegueDeServicio using partial update
        DespliegueDeServicio partialUpdatedDespliegueDeServicio = new DespliegueDeServicio();
        partialUpdatedDespliegueDeServicio.setId(despliegueDeServicio.getId());

        restDespliegueDeServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDespliegueDeServicio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDespliegueDeServicio))
            )
            .andExpect(status().isOk());

        // Validate the DespliegueDeServicio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDespliegueDeServicioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDespliegueDeServicio, despliegueDeServicio),
            getPersistedDespliegueDeServicio(despliegueDeServicio)
        );
    }

    @Test
    @Transactional
    void fullUpdateDespliegueDeServicioWithPatch() throws Exception {
        // Initialize the database
        insertedDespliegueDeServicio = despliegueDeServicioRepository.saveAndFlush(despliegueDeServicio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the despliegueDeServicio using partial update
        DespliegueDeServicio partialUpdatedDespliegueDeServicio = new DespliegueDeServicio();
        partialUpdatedDespliegueDeServicio.setId(despliegueDeServicio.getId());

        partialUpdatedDespliegueDeServicio.version(UPDATED_VERSION).desplegadoEn(UPDATED_DESPLEGADO_EN);

        restDespliegueDeServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDespliegueDeServicio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDespliegueDeServicio))
            )
            .andExpect(status().isOk());

        // Validate the DespliegueDeServicio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDespliegueDeServicioUpdatableFieldsEquals(
            partialUpdatedDespliegueDeServicio,
            getPersistedDespliegueDeServicio(partialUpdatedDespliegueDeServicio)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDespliegueDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        despliegueDeServicio.setId(longCount.incrementAndGet());

        // Create the DespliegueDeServicio
        DespliegueDeServicioDTO despliegueDeServicioDTO = despliegueDeServicioMapper.toDto(despliegueDeServicio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDespliegueDeServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, despliegueDeServicioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(despliegueDeServicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DespliegueDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDespliegueDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        despliegueDeServicio.setId(longCount.incrementAndGet());

        // Create the DespliegueDeServicio
        DespliegueDeServicioDTO despliegueDeServicioDTO = despliegueDeServicioMapper.toDto(despliegueDeServicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDespliegueDeServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(despliegueDeServicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DespliegueDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDespliegueDeServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        despliegueDeServicio.setId(longCount.incrementAndGet());

        // Create the DespliegueDeServicio
        DespliegueDeServicioDTO despliegueDeServicioDTO = despliegueDeServicioMapper.toDto(despliegueDeServicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDespliegueDeServicioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(despliegueDeServicioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DespliegueDeServicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDespliegueDeServicio() throws Exception {
        // Initialize the database
        insertedDespliegueDeServicio = despliegueDeServicioRepository.saveAndFlush(despliegueDeServicio);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the despliegueDeServicio
        restDespliegueDeServicioMockMvc
            .perform(delete(ENTITY_API_URL_ID, despliegueDeServicio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return despliegueDeServicioRepository.count();
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

    protected DespliegueDeServicio getPersistedDespliegueDeServicio(DespliegueDeServicio despliegueDeServicio) {
        return despliegueDeServicioRepository.findById(despliegueDeServicio.getId()).orElseThrow();
    }

    protected void assertPersistedDespliegueDeServicioToMatchAllProperties(DespliegueDeServicio expectedDespliegueDeServicio) {
        assertDespliegueDeServicioAllPropertiesEquals(
            expectedDespliegueDeServicio,
            getPersistedDespliegueDeServicio(expectedDespliegueDeServicio)
        );
    }

    protected void assertPersistedDespliegueDeServicioToMatchUpdatableProperties(DespliegueDeServicio expectedDespliegueDeServicio) {
        assertDespliegueDeServicioAllUpdatablePropertiesEquals(
            expectedDespliegueDeServicio,
            getPersistedDespliegueDeServicio(expectedDespliegueDeServicio)
        );
    }
}

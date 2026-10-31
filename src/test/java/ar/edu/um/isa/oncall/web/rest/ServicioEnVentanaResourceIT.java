package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.ServicioEnVentanaAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.domain.ServicioEnVentana;
import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import ar.edu.um.isa.oncall.repository.ServicioEnVentanaRepository;
import ar.edu.um.isa.oncall.service.ServicioEnVentanaService;
import ar.edu.um.isa.oncall.service.dto.ServicioEnVentanaDTO;
import ar.edu.um.isa.oncall.service.mapper.ServicioEnVentanaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ServicioEnVentanaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ServicioEnVentanaResourceIT {

    private static final String DEFAULT_NOTA = "AAAAAAAAAA";
    private static final String UPDATED_NOTA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/servicio-en-ventanas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServicioEnVentanaRepository servicioEnVentanaRepository;

    @Mock
    private ServicioEnVentanaRepository servicioEnVentanaRepositoryMock;

    @Autowired
    private ServicioEnVentanaMapper servicioEnVentanaMapper;

    @Mock
    private ServicioEnVentanaService servicioEnVentanaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServicioEnVentanaMockMvc;

    private ServicioEnVentana servicioEnVentana;

    private ServicioEnVentana insertedServicioEnVentana;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServicioEnVentana createEntity(EntityManager em) {
        ServicioEnVentana servicioEnVentana = new ServicioEnVentana().nota(DEFAULT_NOTA);
        // Add required entity
        VentanaDeMantenimiento ventanaDeMantenimiento;
        if (TestUtil.findAll(em, VentanaDeMantenimiento.class).isEmpty()) {
            ventanaDeMantenimiento = VentanaDeMantenimientoResourceIT.createEntity();
            em.persist(ventanaDeMantenimiento);
            em.flush();
        } else {
            ventanaDeMantenimiento = TestUtil.findAll(em, VentanaDeMantenimiento.class).get(0);
        }
        servicioEnVentana.setVentana(ventanaDeMantenimiento);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        servicioEnVentana.setServicio(servicio);
        return servicioEnVentana;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServicioEnVentana createUpdatedEntity(EntityManager em) {
        ServicioEnVentana updatedServicioEnVentana = new ServicioEnVentana().nota(UPDATED_NOTA);
        // Add required entity
        VentanaDeMantenimiento ventanaDeMantenimiento;
        if (TestUtil.findAll(em, VentanaDeMantenimiento.class).isEmpty()) {
            ventanaDeMantenimiento = VentanaDeMantenimientoResourceIT.createUpdatedEntity();
            em.persist(ventanaDeMantenimiento);
            em.flush();
        } else {
            ventanaDeMantenimiento = TestUtil.findAll(em, VentanaDeMantenimiento.class).get(0);
        }
        updatedServicioEnVentana.setVentana(ventanaDeMantenimiento);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createUpdatedEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        updatedServicioEnVentana.setServicio(servicio);
        return updatedServicioEnVentana;
    }

    @BeforeEach
    void initTest() {
        servicioEnVentana = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedServicioEnVentana != null) {
            servicioEnVentanaRepository.delete(insertedServicioEnVentana);
            insertedServicioEnVentana = null;
        }
    }

    @Test
    @Transactional
    void createServicioEnVentana() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServicioEnVentana
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);
        var returnedServicioEnVentanaDTO = om.readValue(
            restServicioEnVentanaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioEnVentanaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServicioEnVentanaDTO.class
        );

        // Validate the ServicioEnVentana in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServicioEnVentana = servicioEnVentanaMapper.toEntity(returnedServicioEnVentanaDTO);
        assertServicioEnVentanaUpdatableFieldsEquals(returnedServicioEnVentana, getPersistedServicioEnVentana(returnedServicioEnVentana));

        insertedServicioEnVentana = returnedServicioEnVentana;
    }

    @Test
    @Transactional
    void createServicioEnVentanaWithExistingId() throws Exception {
        // Create the ServicioEnVentana with an existing ID
        servicioEnVentana.setId(1L);
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServicioEnVentanaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioEnVentanaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServicioEnVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllServicioEnVentanas() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        // Get all the servicioEnVentanaList
        restServicioEnVentanaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicioEnVentana.getId().intValue())))
            .andExpect(jsonPath("$.[*].nota").value(hasItem(DEFAULT_NOTA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServicioEnVentanasWithEagerRelationshipsIsEnabled() throws Exception {
        when(servicioEnVentanaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServicioEnVentanaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(servicioEnVentanaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServicioEnVentanasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(servicioEnVentanaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServicioEnVentanaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(servicioEnVentanaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getServicioEnVentana() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        // Get the servicioEnVentana
        restServicioEnVentanaMockMvc
            .perform(get(ENTITY_API_URL_ID, servicioEnVentana.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(servicioEnVentana.getId().intValue()))
            .andExpect(jsonPath("$.nota").value(DEFAULT_NOTA));
    }

    @Test
    @Transactional
    void getNonExistingServicioEnVentana() throws Exception {
        // Get the servicioEnVentana
        restServicioEnVentanaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServicioEnVentana() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicioEnVentana
        ServicioEnVentana updatedServicioEnVentana = servicioEnVentanaRepository.findById(servicioEnVentana.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServicioEnVentana are not directly saved in db
        em.detach(updatedServicioEnVentana);
        updatedServicioEnVentana.nota(UPDATED_NOTA);
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(updatedServicioEnVentana);

        restServicioEnVentanaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicioEnVentanaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioEnVentanaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServicioEnVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServicioEnVentanaToMatchAllProperties(updatedServicioEnVentana);
    }

    @Test
    @Transactional
    void putNonExistingServicioEnVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioEnVentana.setId(longCount.incrementAndGet());

        // Create the ServicioEnVentana
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicioEnVentanaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicioEnVentanaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioEnVentanaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicioEnVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServicioEnVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioEnVentana.setId(longCount.incrementAndGet());

        // Create the ServicioEnVentana
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioEnVentanaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioEnVentanaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicioEnVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServicioEnVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioEnVentana.setId(longCount.incrementAndGet());

        // Create the ServicioEnVentana
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioEnVentanaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioEnVentanaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServicioEnVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServicioEnVentanaWithPatch() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicioEnVentana using partial update
        ServicioEnVentana partialUpdatedServicioEnVentana = new ServicioEnVentana();
        partialUpdatedServicioEnVentana.setId(servicioEnVentana.getId());

        partialUpdatedServicioEnVentana.nota(UPDATED_NOTA);

        restServicioEnVentanaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicioEnVentana.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServicioEnVentana))
            )
            .andExpect(status().isOk());

        // Validate the ServicioEnVentana in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServicioEnVentanaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServicioEnVentana, servicioEnVentana),
            getPersistedServicioEnVentana(servicioEnVentana)
        );
    }

    @Test
    @Transactional
    void fullUpdateServicioEnVentanaWithPatch() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicioEnVentana using partial update
        ServicioEnVentana partialUpdatedServicioEnVentana = new ServicioEnVentana();
        partialUpdatedServicioEnVentana.setId(servicioEnVentana.getId());

        partialUpdatedServicioEnVentana.nota(UPDATED_NOTA);

        restServicioEnVentanaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicioEnVentana.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServicioEnVentana))
            )
            .andExpect(status().isOk());

        // Validate the ServicioEnVentana in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServicioEnVentanaUpdatableFieldsEquals(
            partialUpdatedServicioEnVentana,
            getPersistedServicioEnVentana(partialUpdatedServicioEnVentana)
        );
    }

    @Test
    @Transactional
    void patchNonExistingServicioEnVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioEnVentana.setId(longCount.incrementAndGet());

        // Create the ServicioEnVentana
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicioEnVentanaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, servicioEnVentanaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(servicioEnVentanaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicioEnVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServicioEnVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioEnVentana.setId(longCount.incrementAndGet());

        // Create the ServicioEnVentana
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioEnVentanaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(servicioEnVentanaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicioEnVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServicioEnVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioEnVentana.setId(longCount.incrementAndGet());

        // Create the ServicioEnVentana
        ServicioEnVentanaDTO servicioEnVentanaDTO = servicioEnVentanaMapper.toDto(servicioEnVentana);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioEnVentanaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(servicioEnVentanaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServicioEnVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServicioEnVentana() throws Exception {
        // Initialize the database
        insertedServicioEnVentana = servicioEnVentanaRepository.saveAndFlush(servicioEnVentana);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the servicioEnVentana
        restServicioEnVentanaMockMvc
            .perform(delete(ENTITY_API_URL_ID, servicioEnVentana.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return servicioEnVentanaRepository.count();
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

    protected ServicioEnVentana getPersistedServicioEnVentana(ServicioEnVentana servicioEnVentana) {
        return servicioEnVentanaRepository.findById(servicioEnVentana.getId()).orElseThrow();
    }

    protected void assertPersistedServicioEnVentanaToMatchAllProperties(ServicioEnVentana expectedServicioEnVentana) {
        assertServicioEnVentanaAllPropertiesEquals(expectedServicioEnVentana, getPersistedServicioEnVentana(expectedServicioEnVentana));
    }

    protected void assertPersistedServicioEnVentanaToMatchUpdatableProperties(ServicioEnVentana expectedServicioEnVentana) {
        assertServicioEnVentanaAllUpdatablePropertiesEquals(
            expectedServicioEnVentana,
            getPersistedServicioEnVentana(expectedServicioEnVentana)
        );
    }
}

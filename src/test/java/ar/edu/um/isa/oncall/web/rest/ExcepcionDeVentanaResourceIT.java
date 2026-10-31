package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.ExcepcionDeVentanaAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.ExcepcionDeVentana;
import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import ar.edu.um.isa.oncall.domain.enumeration.TipoExcepcionDeVentana;
import ar.edu.um.isa.oncall.repository.ExcepcionDeVentanaRepository;
import ar.edu.um.isa.oncall.service.ExcepcionDeVentanaService;
import ar.edu.um.isa.oncall.service.dto.ExcepcionDeVentanaDTO;
import ar.edu.um.isa.oncall.service.mapper.ExcepcionDeVentanaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
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
 * Integration tests for the {@link ExcepcionDeVentanaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ExcepcionDeVentanaResourceIT {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.parse("2023-12-04");

    private static final TipoExcepcionDeVentana DEFAULT_TIPO = TipoExcepcionDeVentana.CANCELADA;
    private static final TipoExcepcionDeVentana UPDATED_TIPO = TipoExcepcionDeVentana.REPROGRAMADA;

    private static final Instant DEFAULT_NUEVO_DESDE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NUEVO_DESDE = Instant.ofEpochMilli(1701729143509L);

    private static final Instant DEFAULT_NUEVO_HASTA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NUEVO_HASTA = Instant.ofEpochMilli(1701729143509L);

    private static final String DEFAULT_MOTIVO = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/excepcion-de-ventanas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExcepcionDeVentanaRepository excepcionDeVentanaRepository;

    @Mock
    private ExcepcionDeVentanaRepository excepcionDeVentanaRepositoryMock;

    @Autowired
    private ExcepcionDeVentanaMapper excepcionDeVentanaMapper;

    @Mock
    private ExcepcionDeVentanaService excepcionDeVentanaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExcepcionDeVentanaMockMvc;

    private ExcepcionDeVentana excepcionDeVentana;

    private ExcepcionDeVentana insertedExcepcionDeVentana;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExcepcionDeVentana createEntity(EntityManager em) {
        ExcepcionDeVentana excepcionDeVentana = new ExcepcionDeVentana()
            .fecha(DEFAULT_FECHA)
            .tipo(DEFAULT_TIPO)
            .nuevoDesde(DEFAULT_NUEVO_DESDE)
            .nuevoHasta(DEFAULT_NUEVO_HASTA)
            .motivo(DEFAULT_MOTIVO);
        // Add required entity
        VentanaDeMantenimiento ventanaDeMantenimiento;
        if (TestUtil.findAll(em, VentanaDeMantenimiento.class).isEmpty()) {
            ventanaDeMantenimiento = VentanaDeMantenimientoResourceIT.createEntity();
            em.persist(ventanaDeMantenimiento);
            em.flush();
        } else {
            ventanaDeMantenimiento = TestUtil.findAll(em, VentanaDeMantenimiento.class).get(0);
        }
        excepcionDeVentana.setVentana(ventanaDeMantenimiento);
        return excepcionDeVentana;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExcepcionDeVentana createUpdatedEntity(EntityManager em) {
        ExcepcionDeVentana updatedExcepcionDeVentana = new ExcepcionDeVentana()
            .fecha(UPDATED_FECHA)
            .tipo(UPDATED_TIPO)
            .nuevoDesde(UPDATED_NUEVO_DESDE)
            .nuevoHasta(UPDATED_NUEVO_HASTA)
            .motivo(UPDATED_MOTIVO);
        // Add required entity
        VentanaDeMantenimiento ventanaDeMantenimiento;
        if (TestUtil.findAll(em, VentanaDeMantenimiento.class).isEmpty()) {
            ventanaDeMantenimiento = VentanaDeMantenimientoResourceIT.createUpdatedEntity();
            em.persist(ventanaDeMantenimiento);
            em.flush();
        } else {
            ventanaDeMantenimiento = TestUtil.findAll(em, VentanaDeMantenimiento.class).get(0);
        }
        updatedExcepcionDeVentana.setVentana(ventanaDeMantenimiento);
        return updatedExcepcionDeVentana;
    }

    @BeforeEach
    void initTest() {
        excepcionDeVentana = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedExcepcionDeVentana != null) {
            excepcionDeVentanaRepository.delete(insertedExcepcionDeVentana);
            insertedExcepcionDeVentana = null;
        }
    }

    @Test
    @Transactional
    void createExcepcionDeVentana() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExcepcionDeVentana
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);
        var returnedExcepcionDeVentanaDTO = om.readValue(
            restExcepcionDeVentanaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(excepcionDeVentanaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExcepcionDeVentanaDTO.class
        );

        // Validate the ExcepcionDeVentana in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExcepcionDeVentana = excepcionDeVentanaMapper.toEntity(returnedExcepcionDeVentanaDTO);
        assertExcepcionDeVentanaUpdatableFieldsEquals(
            returnedExcepcionDeVentana,
            getPersistedExcepcionDeVentana(returnedExcepcionDeVentana)
        );

        insertedExcepcionDeVentana = returnedExcepcionDeVentana;
    }

    @Test
    @Transactional
    void createExcepcionDeVentanaWithExistingId() throws Exception {
        // Create the ExcepcionDeVentana with an existing ID
        excepcionDeVentana.setId(1L);
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExcepcionDeVentanaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(excepcionDeVentanaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExcepcionDeVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        excepcionDeVentana.setFecha(null);

        // Create the ExcepcionDeVentana, which fails.
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        restExcepcionDeVentanaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(excepcionDeVentanaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        excepcionDeVentana.setTipo(null);

        // Create the ExcepcionDeVentana, which fails.
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        restExcepcionDeVentanaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(excepcionDeVentanaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExcepcionDeVentanas() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get all the excepcionDeVentanaList
        restExcepcionDeVentanaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(excepcionDeVentana.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].nuevoDesde").value(hasItem(DEFAULT_NUEVO_DESDE.toString())))
            .andExpect(jsonPath("$.[*].nuevoHasta").value(hasItem(DEFAULT_NUEVO_HASTA.toString())))
            .andExpect(jsonPath("$.[*].motivo").value(hasItem(DEFAULT_MOTIVO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExcepcionDeVentanasWithEagerRelationshipsIsEnabled() throws Exception {
        when(excepcionDeVentanaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExcepcionDeVentanaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(excepcionDeVentanaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExcepcionDeVentanasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(excepcionDeVentanaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExcepcionDeVentanaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(excepcionDeVentanaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getExcepcionDeVentana() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        // Get the excepcionDeVentana
        restExcepcionDeVentanaMockMvc
            .perform(get(ENTITY_API_URL_ID, excepcionDeVentana.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(excepcionDeVentana.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.nuevoDesde").value(DEFAULT_NUEVO_DESDE.toString()))
            .andExpect(jsonPath("$.nuevoHasta").value(DEFAULT_NUEVO_HASTA.toString()))
            .andExpect(jsonPath("$.motivo").value(DEFAULT_MOTIVO));
    }

    @Test
    @Transactional
    void getNonExistingExcepcionDeVentana() throws Exception {
        // Get the excepcionDeVentana
        restExcepcionDeVentanaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExcepcionDeVentana() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the excepcionDeVentana
        ExcepcionDeVentana updatedExcepcionDeVentana = excepcionDeVentanaRepository.findById(excepcionDeVentana.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExcepcionDeVentana are not directly saved in db
        em.detach(updatedExcepcionDeVentana);
        updatedExcepcionDeVentana
            .fecha(UPDATED_FECHA)
            .tipo(UPDATED_TIPO)
            .nuevoDesde(UPDATED_NUEVO_DESDE)
            .nuevoHasta(UPDATED_NUEVO_HASTA)
            .motivo(UPDATED_MOTIVO);
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(updatedExcepcionDeVentana);

        restExcepcionDeVentanaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, excepcionDeVentanaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(excepcionDeVentanaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExcepcionDeVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExcepcionDeVentanaToMatchAllProperties(updatedExcepcionDeVentana);
    }

    @Test
    @Transactional
    void putNonExistingExcepcionDeVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        excepcionDeVentana.setId(longCount.incrementAndGet());

        // Create the ExcepcionDeVentana
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExcepcionDeVentanaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, excepcionDeVentanaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(excepcionDeVentanaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExcepcionDeVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExcepcionDeVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        excepcionDeVentana.setId(longCount.incrementAndGet());

        // Create the ExcepcionDeVentana
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcepcionDeVentanaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(excepcionDeVentanaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExcepcionDeVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExcepcionDeVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        excepcionDeVentana.setId(longCount.incrementAndGet());

        // Create the ExcepcionDeVentana
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcepcionDeVentanaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(excepcionDeVentanaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExcepcionDeVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExcepcionDeVentanaWithPatch() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the excepcionDeVentana using partial update
        ExcepcionDeVentana partialUpdatedExcepcionDeVentana = new ExcepcionDeVentana();
        partialUpdatedExcepcionDeVentana.setId(excepcionDeVentana.getId());

        partialUpdatedExcepcionDeVentana.motivo(UPDATED_MOTIVO);

        restExcepcionDeVentanaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExcepcionDeVentana.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExcepcionDeVentana))
            )
            .andExpect(status().isOk());

        // Validate the ExcepcionDeVentana in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExcepcionDeVentanaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExcepcionDeVentana, excepcionDeVentana),
            getPersistedExcepcionDeVentana(excepcionDeVentana)
        );
    }

    @Test
    @Transactional
    void fullUpdateExcepcionDeVentanaWithPatch() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the excepcionDeVentana using partial update
        ExcepcionDeVentana partialUpdatedExcepcionDeVentana = new ExcepcionDeVentana();
        partialUpdatedExcepcionDeVentana.setId(excepcionDeVentana.getId());

        partialUpdatedExcepcionDeVentana
            .fecha(UPDATED_FECHA)
            .tipo(UPDATED_TIPO)
            .nuevoDesde(UPDATED_NUEVO_DESDE)
            .nuevoHasta(UPDATED_NUEVO_HASTA)
            .motivo(UPDATED_MOTIVO);

        restExcepcionDeVentanaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExcepcionDeVentana.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExcepcionDeVentana))
            )
            .andExpect(status().isOk());

        // Validate the ExcepcionDeVentana in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExcepcionDeVentanaUpdatableFieldsEquals(
            partialUpdatedExcepcionDeVentana,
            getPersistedExcepcionDeVentana(partialUpdatedExcepcionDeVentana)
        );
    }

    @Test
    @Transactional
    void patchNonExistingExcepcionDeVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        excepcionDeVentana.setId(longCount.incrementAndGet());

        // Create the ExcepcionDeVentana
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExcepcionDeVentanaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, excepcionDeVentanaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(excepcionDeVentanaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExcepcionDeVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExcepcionDeVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        excepcionDeVentana.setId(longCount.incrementAndGet());

        // Create the ExcepcionDeVentana
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcepcionDeVentanaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(excepcionDeVentanaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExcepcionDeVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExcepcionDeVentana() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        excepcionDeVentana.setId(longCount.incrementAndGet());

        // Create the ExcepcionDeVentana
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = excepcionDeVentanaMapper.toDto(excepcionDeVentana);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcepcionDeVentanaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(excepcionDeVentanaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExcepcionDeVentana in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExcepcionDeVentana() throws Exception {
        // Initialize the database
        insertedExcepcionDeVentana = excepcionDeVentanaRepository.saveAndFlush(excepcionDeVentana);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the excepcionDeVentana
        restExcepcionDeVentanaMockMvc
            .perform(delete(ENTITY_API_URL_ID, excepcionDeVentana.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return excepcionDeVentanaRepository.count();
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

    protected ExcepcionDeVentana getPersistedExcepcionDeVentana(ExcepcionDeVentana excepcionDeVentana) {
        return excepcionDeVentanaRepository.findById(excepcionDeVentana.getId()).orElseThrow();
    }

    protected void assertPersistedExcepcionDeVentanaToMatchAllProperties(ExcepcionDeVentana expectedExcepcionDeVentana) {
        assertExcepcionDeVentanaAllPropertiesEquals(expectedExcepcionDeVentana, getPersistedExcepcionDeVentana(expectedExcepcionDeVentana));
    }

    protected void assertPersistedExcepcionDeVentanaToMatchUpdatableProperties(ExcepcionDeVentana expectedExcepcionDeVentana) {
        assertExcepcionDeVentanaAllUpdatablePropertiesEquals(
            expectedExcepcionDeVentana,
            getPersistedExcepcionDeVentana(expectedExcepcionDeVentana)
        );
    }
}

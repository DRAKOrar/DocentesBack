package com.pagoDocente;

import com.pagoDocente.dto.PagoRequest;
import com.pagoDocente.dto.PagoResponse;
import com.pagoDocente.entity.DocentePago;
import com.pagoDocente.repository.DocentePagoRepository;
import com.pagoDocente.service.servicieImpl.PagoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PagoServiceImplTest {

	@Mock
	private DocentePagoRepository pagoRepository;

	@InjectMocks
	private PagoServiceImpl pagoService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void calcularPagoTiempoCompleto() {
		// Arrange
		PagoRequest request = new PagoRequest("tiempo_completo", 10, 5, 2, 1);
		when(pagoRepository.save(any(DocentePago.class))).thenReturn(null);

		// Act
		PagoResponse response = pagoService.calcularPago(request);

		// Assert
		assertEquals(460000.0, response.getSalarioBruto());
		assertEquals(36800.0, response.getDescuentoParafiscales());
		assertEquals(423200.0, response.getSalarioNeto());

		verify(pagoRepository).save(any(DocentePago.class));
	}
	@Test
	void calcularPagoSoloNocturnasTiempoCompleto() {
		PagoRequest request = new PagoRequest("tiempo_completo", 0, 5, 0, 0);
		when(pagoRepository.save(any(DocentePago.class))).thenReturn(null);

		double bruto = 5 * 30000; // 150000
		double parafiscales = bruto * 0.08; // 12000
		double neto = bruto - parafiscales; // 138000

		PagoResponse response = pagoService.calcularPago(request);

		assertEquals(150000.0, response.getSalarioBruto());
		assertEquals(12000.0, response.getDescuentoParafiscales());
		assertEquals(138000.0, response.getSalarioNeto());
		verify(pagoRepository).save(any(DocentePago.class));
	}

	@Test
	void calcularPagoSoloDominicalesMedioTiempo() {
		PagoRequest request = new PagoRequest("medio_tiempo", 0, 0, 4, 0);
		when(pagoRepository.save(any(DocentePago.class))).thenReturn(null);

		double bruto = 4 * 21000; // 84000
		double parafiscales = bruto * 0.08; // 6720
		double neto = bruto - parafiscales; // 77280

		PagoResponse response = pagoService.calcularPago(request);

		assertEquals(84000.0, response.getSalarioBruto());
		assertEquals(6720.0, response.getDescuentoParafiscales());
		assertEquals(77280.0, response.getSalarioNeto());
		verify(pagoRepository).save(any(DocentePago.class));
	}

	@Test
	void calcularPagoGrandesCantidadesTiempoCompleto() {
		PagoRequest request = new PagoRequest("tiempo_completo", 100, 50, 20, 10);
		when(pagoRepository.save(any(DocentePago.class))).thenReturn(null);

		double bruto = 100*20000 + 50*30000 + 20*35000 + 10*40000; // 2000000 + 1500000 + 700000 + 400000 = 4600000
		double parafiscales = bruto * 0.08; // 368000
		double neto = bruto - parafiscales; // 4232000

		PagoResponse response = pagoService.calcularPago(request);

		assertEquals(4600000.0, response.getSalarioBruto());
		assertEquals(368000.0, response.getDescuentoParafiscales());
		assertEquals(4232000.0, response.getSalarioNeto());
		verify(pagoRepository).save(any(DocentePago.class));
	}

	@Test
	void calcularPagoContratoConEspacios() {
		PagoRequest request = new PagoRequest(" tiempo_completo ", 1, 1, 1, 1);
		when(pagoRepository.save(any(DocentePago.class))).thenReturn(null);

		assertThrows(IllegalArgumentException.class, () -> pagoService.calcularPago(request));
	}

	@Test
	void calcularPagoContratoNull() {
		PagoRequest request = new PagoRequest(null, 1, 1, 1, 1);
		assertThrows(IllegalArgumentException.class, () -> pagoService.calcularPago(request));
	}

	@Test
	void calcularPagoHorasNegativas() {
		PagoRequest request = new PagoRequest("medio_tiempo", -1, 2, 3, 4);
		PagoResponse response = pagoService.calcularPago(request);

		// Si no hay validación, se permite; ajusta esto si implementás restricciones.
		double bruto = -1*12000 + 2*18000 + 3*21000 + 4*24000;
		double parafiscales = bruto * 0.08;
		double neto = bruto - parafiscales;

		when(pagoRepository.save(any(DocentePago.class))).thenReturn(null);

		assertEquals(bruto, response.getSalarioBruto());
		assertEquals(parafiscales, response.getDescuentoParafiscales());
		assertEquals(neto, response.getSalarioNeto());
	}

	@Test
	void calcularPagoSoloUnaHoraFestivaMedioTiempo() {
		PagoRequest request = new PagoRequest("medio_tiempo", 0, 0, 0, 1);
		when(pagoRepository.save(any(DocentePago.class))).thenReturn(null);

		PagoResponse response = pagoService.calcularPago(request);

		assertEquals(24000.0, response.getSalarioBruto());
		assertEquals(1920.0, response.getDescuentoParafiscales());
		assertEquals(22080.0, response.getSalarioNeto());
	}

	@Test
	void calcularPagoSoloUnaHoraDiurnaTiempoCompleto() {
		PagoRequest request = new PagoRequest("tiempo_completo", 1, 0, 0, 0);
		when(pagoRepository.save(any(DocentePago.class))).thenReturn(null);

		PagoResponse response = pagoService.calcularPago(request);

		assertEquals(20000.0, response.getSalarioBruto());
		assertEquals(1600.0, response.getDescuentoParafiscales());
		assertEquals(18400.0, response.getSalarioNeto());
	}

	@Test
	void calcularPagoFormatoContratoMixto() {
		PagoRequest request = new PagoRequest("MeDiO_TiEmPo", 2, 2, 2, 2);
		when(pagoRepository.save(any(DocentePago.class))).thenReturn(null);

		double bruto = (2*12000) + (2*18000) + (2*21000) + (2*24000); // 24k + 36k + 42k + 48k = 150k
		double parafiscales = bruto * 0.08; // 12k
		double neto = bruto - parafiscales; // 138k

		PagoResponse response = pagoService.calcularPago(request);

		assertEquals(150000.0, response.getSalarioBruto());
		assertEquals(12000.0, response.getDescuentoParafiscales());
		assertEquals(138000.0, response.getSalarioNeto());
	}

	@Test
	void calcularPagoTipoContratoVacio() {
		PagoRequest request = new PagoRequest("", 1, 1, 1, 1);
		assertThrows(IllegalArgumentException.class, () -> pagoService.calcularPago(request));
	}

	@Test
	void calcularPagoSoloHorasNocturnasMedioTiempo() {
		PagoRequest request = new PagoRequest("medio_tiempo", 0, 5, 0, 0);
		when(pagoRepository.save(any(DocentePago.class))).thenReturn(null);

		double bruto = 5 * 18000; // 90000
		double parafiscales = bruto * 0.08; // 7200
		double neto = bruto - parafiscales; // 82800

		PagoResponse response = pagoService.calcularPago(request);

		assertEquals(90000.0, response.getSalarioBruto());
		assertEquals(7200.0, response.getDescuentoParafiscales());
		assertEquals(82800.0, response.getSalarioNeto());
	}

	@Test
	void calcularPagoTodoCeroMedioTiempo() {
		PagoRequest request = new PagoRequest("medio_tiempo", 0, 0, 0, 0);
		when(pagoRepository.save(any(DocentePago.class))).thenReturn(null);

		PagoResponse response = pagoService.calcularPago(request);

		assertEquals(0.0, response.getSalarioBruto());
		assertEquals(0.0, response.getDescuentoParafiscales());
		assertEquals(0.0, response.getSalarioNeto());
		verify(pagoRepository).save(any(DocentePago.class));
	}



	@Test
	void calcularPagoMedioTiempo() {
		// Arrange
		PagoRequest request = new PagoRequest("medio_tiempo", 10, 5, 2, 1);
		when(pagoRepository.save(any(DocentePago.class))).thenReturn(null);

		// Act
		PagoResponse response = pagoService.calcularPago(request);

		// Assert
		assertEquals(276000.0, response.getSalarioBruto());
		assertEquals(22080.0, response.getDescuentoParafiscales());
		assertEquals(253920.0, response.getSalarioNeto());

		verify(pagoRepository).save(any(DocentePago.class));
	}

	@Test
	void calcularPagoTipoContratoInvalido() {
		// Arrange
		PagoRequest request = new PagoRequest("parcial", 10, 5, 2, 1);

		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> pagoService.calcularPago(request));
	}

	@Test
	void calcularPagoHorasCero() {
		PagoRequest request = new PagoRequest("tiempo_completo", 0, 0, 0, 0);
		when(pagoRepository.save(any(DocentePago.class))).thenReturn(null);

		PagoResponse response = pagoService.calcularPago(request);

		assertEquals(0.0, response.getSalarioBruto());
		assertEquals(0.0, response.getDescuentoParafiscales());
		assertEquals(0.0, response.getSalarioNeto());
		verify(pagoRepository).save(any(DocentePago.class));
	}

	@Test
	void calcularPagoSoloHorasDiurnasMedioTiempo() {
		PagoRequest request = new PagoRequest("medio_tiempo", 8, 0, 0, 0);
		when(pagoRepository.save(any(DocentePago.class))).thenReturn(null);

		PagoResponse response = pagoService.calcularPago(request);

		double bruto = 8 * 12000; // 96,000
		double parafiscales = bruto * 0.08; // 7680
		double neto = bruto - parafiscales; // 88320

		assertEquals(96000.0, response.getSalarioBruto());
		assertEquals(7680.0, response.getDescuentoParafiscales());
		assertEquals(88320.0, response.getSalarioNeto());
		verify(pagoRepository).save(any(DocentePago.class));
	}

	@Test
	void calcularPagoSoloHorasFestivasTiempoCompleto() {
		PagoRequest request = new PagoRequest("tiempo_completo", 0, 0, 0, 4);
		when(pagoRepository.save(any(DocentePago.class))).thenReturn(null);

		double bruto = 4 * 40000; // 160000
		double parafiscales = bruto * 0.08; // 12800
		double neto = bruto - parafiscales; // 147200

		PagoResponse response = pagoService.calcularPago(request);

		assertEquals(160000.0, response.getSalarioBruto());
		assertEquals(12800.0, response.getDescuentoParafiscales());
		assertEquals(147200.0, response.getSalarioNeto());
		verify(pagoRepository).save(any(DocentePago.class));
	}

	@Test
	void calcularPagoHorasMixtasMedioTiempo() {
		PagoRequest request = new PagoRequest("medio_tiempo", 2, 4, 1, 3);
		when(pagoRepository.save(any(DocentePago.class))).thenReturn(null);

		double bruto = (2 * 12000) + (4 * 18000) + (1 * 21000) + (3 * 24000); // 24k + 72k + 21k + 72k = 189000
		double parafiscales = bruto * 0.08; // 15120
		double neto = bruto - parafiscales; // 173880

		PagoResponse response = pagoService.calcularPago(request);

		assertEquals(189000.0, response.getSalarioBruto());
		assertEquals(15120.0, response.getDescuentoParafiscales());
		assertEquals(173880.0, response.getSalarioNeto());
		verify(pagoRepository).save(any(DocentePago.class));
	}

	@Test
	void calcularPagoContratoCaseInsensitive() {
		PagoRequest request = new PagoRequest("TIEMPO_COMPLETO", 1, 1, 1, 1);
		when(pagoRepository.save(any(DocentePago.class))).thenReturn(null);

		double bruto = 20000 + 30000 + 35000 + 40000; // 125000
		double parafiscales = bruto * 0.08; // 10000
		double neto = bruto - parafiscales; // 115000

		PagoResponse response = pagoService.calcularPago(request);

		assertEquals(125000.0, response.getSalarioBruto());
		assertEquals(10000.0, response.getDescuentoParafiscales());
		assertEquals(115000.0, response.getSalarioNeto());
		verify(pagoRepository).save(any(DocentePago.class));
	}



}

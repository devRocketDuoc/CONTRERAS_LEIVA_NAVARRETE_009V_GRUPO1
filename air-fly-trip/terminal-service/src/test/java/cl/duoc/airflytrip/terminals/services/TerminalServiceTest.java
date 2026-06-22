package cl.duoc.airflytrip.terminals.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import cl.duoc.airflytrip.terminals.dtos.request.CreateTerminalRequest;
import cl.duoc.airflytrip.terminals.dtos.request.UpdateTerminalRequest;
import cl.duoc.airflytrip.terminals.dtos.response.TerminalResponse;
import cl.duoc.airflytrip.terminals.exceptions.NotFoundException;
import cl.duoc.airflytrip.terminals.models.Terminal;
import cl.duoc.airflytrip.terminals.repositories.TerminalRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TerminalServiceTest {

  @Mock
  private TerminalRepository terminalRepository;

  @InjectMocks
  private TerminalService terminalService;

  @Test
  void testCrearTerminalExitoso() {

    CreateTerminalRequest request = new CreateTerminalRequest();
    request.setName("Terminal Alameda");
    request.setCity("Santiago");
    request.setLocationDescription("Av. Libertador Bernardo O'Higgins 3850");
    request.setLatitude(new BigDecimal("-33.4521"));
    request.setLongitude(new BigDecimal("-70.6795"));

    when(terminalRepository.save(any(Terminal.class))).thenAnswer(invocation -> {
      Terminal terminal = invocation.getArgument(0);
      terminal.setId(4L);
      return terminal;
    });

    TerminalResponse response = terminalService.create(request);

    assertNotNull(response);
    assertEquals(4L, response.getId());
    assertEquals("Terminal Alameda", response.getName());
    assertTrue(response.getActive());
  }

  @Test
  void testBuscarTerminalPorIdFallaCuandoNoExiste() {

    when(terminalRepository.findById(99L)).thenReturn(Optional.empty());

    Exception exception = null;

    try {
      terminalService.findById(99L);
    } catch (Exception ex) {
      exception = ex;
    }

    assertNotNull(exception);
    assertEquals(NotFoundException.class, exception.getClass());
    assertEquals("Terminal not found with id: 99", exception.getMessage());
  }

  @Test
  void testActualizarTerminalMantieneActivoCuandoNoSeInforma() {

    UpdateTerminalRequest request = new UpdateTerminalRequest();
    request.setName("Terminal Pajaritos");
    request.setCity("Santiago");
    request.setLocationDescription("Av. General Oscar Bonilla 2250");
    request.setLatitude(new BigDecimal("-33.4445"));
    request.setLongitude(new BigDecimal("-70.7193"));

    Terminal existing = terminal(4L, "Terminal Alameda", true);

    when(terminalRepository.findById(4L)).thenReturn(Optional.of(existing));
    when(terminalRepository.save(any(Terminal.class))).thenAnswer(invocation -> invocation.getArgument(0));

    TerminalResponse response = terminalService.update(4L, request);

    assertNotNull(response);
    assertEquals("Terminal Pajaritos", response.getName());
    assertTrue(response.getActive());
  }

  @Test
  void testEliminarTerminalDesactivaRegistro() {

    Terminal existing = terminal(4L, "Terminal Alameda", true);

    when(terminalRepository.findById(4L)).thenReturn(Optional.of(existing));
    when(terminalRepository.save(any(Terminal.class))).thenAnswer(invocation -> invocation.getArgument(0));

    terminalService.delete(4L);

    assertFalse(existing.getActive());
  }

  private Terminal terminal(Long id, String name, Boolean active) {
    return Terminal.builder()
        .id(id)
        .name(name)
        .city("Santiago")
        .locationDescription("Ubicacion " + id)
        .latitude(new BigDecimal("-33.4521"))
        .longitude(new BigDecimal("-70.6795"))
        .active(active)
        .build();
  }
}

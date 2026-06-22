package cl.duoc.airflytrip.terminals.controllers;

import cl.duoc.airflytrip.terminals.dtos.request.CreateTerminalRequest;
import cl.duoc.airflytrip.terminals.dtos.request.UpdateTerminalRequest;
import cl.duoc.airflytrip.terminals.dtos.response.TerminalResponse;
import cl.duoc.airflytrip.terminals.services.TerminalService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TerminalControllerTest {

  @Mock
  private TerminalService terminalService;

  @InjectMocks
  private TerminalController terminalController;

  @Test
  void findAllShouldReturnTerminals() {

    when(terminalService.findAll()).thenReturn(List.of(terminalResponse(1L, "Terminal Alameda", true)));

    ResponseEntity<List<TerminalResponse>> result = terminalController.findAll();

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(1, result.getBody().size());
    assertEquals("Terminal Alameda", result.getBody().get(0).getName());
  }

  @Test
  void createShouldReturnCreatedTerminal() {

    CreateTerminalRequest request = new CreateTerminalRequest();
    request.setName("Terminal Alameda");
    request.setCity("Santiago");

    TerminalResponse responseSimulado = terminalResponse(4L, "Terminal Alameda", true);

    when(terminalService.create(any(CreateTerminalRequest.class))).thenReturn(responseSimulado);

    ResponseEntity<TerminalResponse> result = terminalController.create(request);

    assertEquals(HttpStatus.CREATED, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(4L, result.getBody().getId());
    assertEquals("Terminal Alameda", result.getBody().getName());
  }

  @Test
  void updateShouldReturnUpdatedTerminal() {

    UpdateTerminalRequest request = new UpdateTerminalRequest();
    request.setName("Terminal Pajaritos");
    request.setCity("Santiago");

    TerminalResponse responseSimulado = terminalResponse(4L, "Terminal Pajaritos", true);

    when(terminalService.update(4L, request)).thenReturn(responseSimulado);

    ResponseEntity<TerminalResponse> result = terminalController.update(4L, request);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals("Terminal Pajaritos", result.getBody().getName());
  }

  @Test
  void deleteShouldReturnNoContent() {

    ResponseEntity<Void> result = terminalController.delete(4L);

    assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
  }

  private TerminalResponse terminalResponse(Long id, String name, Boolean active) {
    return TerminalResponse.builder()
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

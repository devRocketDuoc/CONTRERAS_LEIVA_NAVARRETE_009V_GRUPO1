package cl.duoc.airflytrip.terminals.services;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.airflytrip.terminals.dtos.request.CreateTerminalRequest;
import cl.duoc.airflytrip.terminals.dtos.request.UpdateTerminalRequest;
import cl.duoc.airflytrip.terminals.dtos.response.TerminalResponse;
import cl.duoc.airflytrip.terminals.exceptions.NotFoundException;
import cl.duoc.airflytrip.terminals.models.Terminal;
import cl.duoc.airflytrip.terminals.repositories.TerminalRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TerminalService {

    private final TerminalRepository terminalRepository;

    public List<TerminalResponse> findAll() {
        return terminalRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TerminalResponse> findActive() {
        return terminalRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TerminalResponse findById(Long id) {
        Terminal terminal = findTerminalById(id);
        return toResponse(terminal);
    }

    public TerminalResponse create(CreateTerminalRequest request) {
        Terminal terminal = Terminal.builder()
                .name(request.getName())
                .city(request.getCity())
                .locationDescription(request.getLocationDescription())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        Terminal savedTerminal = terminalRepository.save(terminal);

        return toResponse(savedTerminal);
    }

    public TerminalResponse update(Long id, UpdateTerminalRequest request) {
        Terminal terminal = findTerminalById(id);

        terminal.setName(request.getName());
        terminal.setCity(request.getCity());
        terminal.setLocationDescription(request.getLocationDescription());
        terminal.setLatitude(request.getLatitude());
        terminal.setLongitude(request.getLongitude());
        terminal.setActive(request.getActive() != null ? request.getActive() : terminal.getActive());

        Terminal updatedTerminal = terminalRepository.save(terminal);

        return toResponse(updatedTerminal);
    }

    public void delete(Long id) {
        Terminal terminal = findTerminalById(id);
        terminal.setActive(false);
        terminalRepository.save(terminal);
    }

    private Terminal findTerminalById(Long id) {
        return terminalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Terminal not found with id: " + id));
    }

    private TerminalResponse toResponse(Terminal terminal) {
        return TerminalResponse.builder()
                .id(terminal.getId())
                .name(terminal.getName())
                .city(terminal.getCity())
                .locationDescription(terminal.getLocationDescription())
                .latitude(terminal.getLatitude())
                .longitude(terminal.getLongitude())
                .active(terminal.getActive())
                .build();
    }
}

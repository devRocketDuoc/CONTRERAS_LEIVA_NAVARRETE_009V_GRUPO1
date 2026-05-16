package cl.duoc.airflytrip.terminals.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.airflytrip.terminals.dtos.request.CreateTerminalRequest;
import cl.duoc.airflytrip.terminals.dtos.request.UpdateTerminalRequest;
import cl.duoc.airflytrip.terminals.dtos.response.TerminalResponse;
import cl.duoc.airflytrip.terminals.services.TerminalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/terminals")
@RequiredArgsConstructor
public class TerminalController {

    private final TerminalService terminalService;

    @GetMapping
    public ResponseEntity<List<TerminalResponse>> findAll() {
        return ResponseEntity.ok(terminalService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<TerminalResponse>> findActive() {
        return ResponseEntity.ok(terminalService.findActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TerminalResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(terminalService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TerminalResponse> create(@Valid @RequestBody CreateTerminalRequest request) {
        TerminalResponse response = terminalService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TerminalResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTerminalRequest request
    ) {
        return ResponseEntity.ok(terminalService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        terminalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package nl.rnsd.eventstore.search.engine.api;


import lombok.RequiredArgsConstructor;
import nl.rnsd.eventstore.search.engine.service.sync.EventStoreSynchronizer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SynchronizationController {

    private final EventStoreSynchronizer eventStoreSynchronizer;

    @GetMapping("/event/sync")
    @Transactional
    public ResponseEntity<String> sync() throws IOException {
        eventStoreSynchronizer.sync();
        return ResponseEntity.ok("Synchronization completed");
    }
}

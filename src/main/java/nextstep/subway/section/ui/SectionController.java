package nextstep.subway.section.ui;

import java.net.URI;
import nextstep.subway.line.application.LineCommandService;
import nextstep.subway.line.application.LineQueryService;
import nextstep.subway.section.application.SectionCommandService;
import nextstep.subway.section.domain.LineSections;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.section.dto.SectionsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/lines/{lineId}/sections")
public class SectionController {

    private final LineQueryService lineQueryService;
    private final LineCommandService lineCommandService;
    private final SectionCommandService sectionCommandService;

    public SectionController(LineQueryService lineQueryService,
                             LineCommandService lineCommandService,
                             SectionCommandService sectionCommandService) {
        this.lineQueryService = lineQueryService;
        this.lineCommandService = lineCommandService;
        this.sectionCommandService = sectionCommandService;
    }

    @GetMapping
    public ResponseEntity<SectionsResponse> getSection(@PathVariable Long lineId) {
        return ResponseEntity.ok()
                             .body(SectionsResponse.of(lineId, lineQueryService.findById(lineId)
                                                                               .getSortedSections()));
    }

    @PostMapping
    public ResponseEntity<SectionsResponse> addSection(@PathVariable Long lineId,
                                                       @RequestBody SectionRequest request) {

        LineSections sections = lineQueryService.findById(lineId).getSections();
        lineCommandService.updateSections(lineId,
                                          sectionCommandService.upsert(sections,
                                                                       request.getUpStationId(),
                                                                       request.getDownStationId(),
                                                                       request.getDistance()));

        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections"))
                             .body(SectionsResponse.of(lineId,
                                                       lineQueryService.findById(lineId)
                                                                       .getSortedSections()));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeSection(@PathVariable Long lineId,
                                              @RequestParam Long stationId) {
        sectionCommandService.delete(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}

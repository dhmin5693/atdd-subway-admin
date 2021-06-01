package nextstep.subway.line.application;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.exception.NotFoundLineException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class LineQueryService {

    private final LineRepository lineRepository;

    public LineQueryService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public List<LineResponse> findLines() {
        return lineRepository.findAll()
                             .stream()
                             .map(LineResponse::of)
                             .collect(toList());
    }

    public LineResponse findLine(Long lineId) {
        Line line = findLineById(lineId);
        return LineResponse.of(line);
    }

    public Line findLineById(Long lineId) {
        return lineRepository.findById(lineId)
                             .orElseThrow(() -> new NotFoundLineException(lineId));
    }
}

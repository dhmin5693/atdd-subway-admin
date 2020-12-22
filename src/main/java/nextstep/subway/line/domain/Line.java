package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.common.domain.BaseEntity;
import nextstep.subway.line.dto.LineRequest;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Line extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;
	private String color;

	public Line(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public void update(LineRequest line) {
		this.name = line.getName();
		this.color = line.getColor();
	}
}

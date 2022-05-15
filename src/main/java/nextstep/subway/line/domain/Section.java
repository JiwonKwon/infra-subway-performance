package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import org.springframework.cache.annotation.Cacheable;
import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Section implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private int distance;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    // 메서드 실행 전에 캐시를 확인하여 최소 하나의 캐시가 존재한다면 값을 반환한다.
    // SpEL 표현식을 활용하여 조건부 캐싱이 가능하다.
    @Cacheable(value = "line", key = "#id")
    public Line getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Boolean equalUpStation(Long stationId) {
        return this.upStation.getId().equals(stationId);
    }

    public Boolean equalUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public Boolean equalDownStation(Long stationId) {
        return this.downStation.getId().equals(stationId);
    }

    public Boolean equalDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    public void updateUpStation(Station station, int newDistance) {
        if (this.distance < newDistance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = station;
        this.distance -= newDistance;
    }

    public void updateDownStation(Station station, int newDistance) {
        if (this.distance < newDistance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.downStation = station;
        this.distance -= newDistance;
    }

    public boolean existDownStation() {
        return this.downStation != null;
    }

    public boolean existUpStation() {
        return this.upStation != null;
    }
}

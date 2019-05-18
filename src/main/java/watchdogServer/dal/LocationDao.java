package watchdogServer.dal;

import java.util.Date;
import java.util.List;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import watchdogServer.logic.LocationEntity;
import watchdogServer.logic.Position;

public interface LocationDao extends MongoRepository<LocationEntity, String> {
	public List<LocationEntity> findAllByTimeBetween(Date from, Date to);
	public GeoResults<LocationEntity> findByPositionNear(Point position, Distance locationDeviationInMeters);
}

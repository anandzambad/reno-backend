package com.reno.availability;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Service
public class RedisAvailabilityService {
    private static final String KEY = "reno:contractors:geo";
    private static final String ONLINE_PREFIX = "reno:contractor:online:";
    private final StringRedisTemplate redis;
    public RedisAvailabilityService(StringRedisTemplate redis){this.redis=redis;}

    public void upsertLocation(long contractorId, double latitude, double longitude){
        redis.opsForGeo().add(KEY, new RedisGeoCommands.GeoLocation<>(String.valueOf(contractorId), new org.springframework.data.geo.Point(longitude, latitude)));
        redis.opsForValue().set(ONLINE_PREFIX+contractorId, "1", Duration.ofMinutes(2));
    }
    public void markOffline(long contractorId){redis.opsForValue().delete(ONLINE_PREFIX+contractorId); redis.opsForGeo().remove(KEY, String.valueOf(contractorId));}
    public boolean isOnline(long contractorId){return Boolean.TRUE.equals(redis.hasKey(ONLINE_PREFIX+contractorId));}
    public List<GeoResult<RedisGeoCommands.GeoLocation<String>>> nearby(double latitude,double longitude,double radiusKm,int limit){
        GeoResults<RedisGeoCommands.GeoLocation<String>> results=redis.opsForGeo().radius(KEY,new Circle(new org.springframework.data.geo.Point(longitude,latitude),new Distance(radiusKm,Metrics.KILOMETERS)),RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().sortAscending().limit(limit));
        return results==null?List.of():results.getContent();
    }
}

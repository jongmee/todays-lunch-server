package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.repository.LocationTagRepository;
import javax.transaction.Transactional;

@Transactional
public class LocationTagService {
  private final LocationTagRepository locationTagRepository;

  public LocationTagService(LocationTagRepository locationTagRepository) {
    this.locationTagRepository = locationTagRepository;
  }

  public LocationTag findLocationTagByName(String name){
    return locationTagRepository.findByName(name).get();
  }
}

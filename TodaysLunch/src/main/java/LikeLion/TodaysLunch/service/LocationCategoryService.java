package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.repository.LocationCategoryRepository;
import javax.transaction.Transactional;

@Transactional
public class LocationCategoryService {
  private final LocationCategoryRepository locationCategoryRepository;

  public LocationCategoryService(LocationCategoryRepository locationCategoryRepository) {
    this.locationCategoryRepository = locationCategoryRepository;
  }
  public LocationCategory findLocationCategoryByName(String name){
    return locationCategoryRepository.findByName(name).get();
  }
}

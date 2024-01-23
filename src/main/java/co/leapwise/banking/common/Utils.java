package co.leapwise.banking.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {
  public <T> T getRandom(List<T> list) {
    return list.get(new Random().nextInt(list.size()));
  }

  public Long getRandomLong(Long startInclusive, Long endInclusive) {
    var range = endInclusive - startInclusive + 1;
    return (long) (new Random().nextDouble() * range) + startInclusive;
  }

  public LocalDateTime getRandomLocalDateTime() {
    var random = new Random();
    var randomDate =
        LocalDate.of(random.nextInt(50) + 1970, random.nextInt(12) + 1, random.nextInt(28) + 1);
    var randomTime = LocalTime.of(random.nextInt(24), random.nextInt(60), random.nextInt(60));
    return LocalDateTime.of(randomDate, randomTime);
  }
}

package co.leapwise.banking.request.params;

import lombok.Data;
import org.springframework.data.domain.PageRequest;

@Data
public class PageableParams {
  private int page = 0;
  private int size = 10;

  public PageRequest getPageRequest() {
    return PageRequest.of(getPage(), getSize());
  }
}

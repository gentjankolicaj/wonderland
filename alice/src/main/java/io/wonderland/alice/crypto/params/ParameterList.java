package io.wonderland.alice.crypto.params;

import io.wonderland.alice.crypto.CipherParameters;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

@Getter
public class ParameterList extends ArrayList<CipherParameters> implements CipherParameters {

  public ParameterList() {
  }

  public ParameterList(CipherParameters... params) {
    super();
    if (ArrayUtils.isNotEmpty(params)) {
      for (CipherParameters param : params) {
        add(param);
      }
    }
  }

  public ParameterList(List<CipherParameters> params) {
    super();
    if (CollectionUtils.isNotEmpty(params)) {
      this.addAll(params);
    }
  }

}

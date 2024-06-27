package io.wonderland.alice.crypto.params;

import io.wonderland.alice.crypto.CipherParameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

@Getter
public class ParameterList extends ArrayList<CipherParameter> implements CipherParameter {

  public ParameterList() {
  }

  public ParameterList(CipherParameter... params) {
    super();
    if (ArrayUtils.isNotEmpty(params)) {
      this.addAll(Arrays.asList(params));
    }
  }

  public ParameterList(List<CipherParameter> params) {
    super();
    if (CollectionUtils.isNotEmpty(params)) {
      this.addAll(params);
    }
  }

}

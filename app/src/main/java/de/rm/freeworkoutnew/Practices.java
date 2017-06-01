package de.rm.freeworkoutnew;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by uwe on 24.09.13.
 */
@Root
public class Practices {
  @ElementList(required=false,entry="practice", inline = true)
  private List<Practice> practice;

   public List<Practice> getPractice() {
       return practice;
    }
}

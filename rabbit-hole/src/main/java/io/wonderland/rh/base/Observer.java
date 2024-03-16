package io.wonderland.rh.base;

public interface Observer<ID,NOTIFICATION> extends Identifiable<ID> {
    void update(NOTIFICATION notification);

}

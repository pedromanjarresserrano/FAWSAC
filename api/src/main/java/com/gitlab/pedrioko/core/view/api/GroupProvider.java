package com.gitlab.pedrioko.core.view.api;

import java.util.List;

public interface GroupProvider extends Provider {

    List<Provider> getContent();

}

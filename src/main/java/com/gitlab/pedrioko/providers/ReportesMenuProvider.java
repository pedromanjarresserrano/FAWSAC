package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import com.gitlab.pedrioko.domain.Reporte;
import org.zkoss.zk.ui.Component;

@Menu
public class ReportesMenuProvider implements MenuProvider {

	@Override
	public String getLabel() {
		return ReflectionZKUtil.getLabel("reportes");
	}

	@Override
	public Component getView() {
		return new CrudView(Reporte.class);
	}

	@Override
	public String getIcon() {
		return "fas fa-chart-pie";
	}

	@Override
	public int getPosition() {
		return 1;
	}

	@Override
	public String getGroup() {
		return "administracion";
	}
}
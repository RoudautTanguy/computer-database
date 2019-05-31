package com.excilys.cdb.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.OrderByEnum;
import com.excilys.cdb.service.ServiceComputer;
import com.excilys.cdb.service.ServicePagination;

@Controller
public class DashboardController {

	private ServiceComputer serviceComputer;
	private ServicePagination servicePagination;
	
	public DashboardController(ServiceComputer serviceComputer, ServicePagination servicePagination) {
		this.serviceComputer = serviceComputer;
		this.servicePagination = servicePagination;
	}

	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

	@GetMapping(value= {"/","/dashboard"})
	public String dashboard(@RequestParam(value = "page", required = false) String pPage,
			@RequestParam(value = "orderBy", required = false) String pOrderBy,
			@RequestParam(value = "search", required = false) String pSearch,
			@RequestParam(value = "size", required = false) String pSize,
			final ModelMap model) throws PageNotFoundException{
		OrderByEnum orderBy = getOrderByAttribute(pOrderBy);
		model.addAttribute("orderBy", orderBy.ordinal());

		int currentPage = getPageAttribute(pPage);
		model.addAttribute("currentPage", currentPage);

		int size = getSizeAttribute(pSize);
		model.addAttribute("size", size);

		int lastPage = getLastPage(size, pSearch);
		model.addAttribute("lastPage", lastPage);

		model.addAttribute("pagination", getPaginationAttribute(currentPage, lastPage));
		model.addAttribute("count", getComputerCount(pSearch));

		Page<DTOComputer> pagination = new Page<>(null,currentPage-1,size,pSearch);
		model.addAttribute("computers", getComputersAttribute(pagination, orderBy));

		return "dashboard";
	}

	@PostMapping("/dashboard") 
	public String deleteComputers(@RequestParam Map<String, String> requestParams) throws ComputerNotFoundException{
		String[] idComputers = requestParams.get("selection").split(",");
		for(String id:idComputers) {
			try {
				serviceComputer.delete(Integer.parseInt(id));
			} catch (NumberFormatException e) {
				throw new ComputerNotFoundException("");
			}
		}
		return "redirect:/dashboard";
	}

	private int getPageAttribute(String page) {
		return servicePagination.getCurrentPage(page);
	}

	private int getSizeAttribute(String pSize) {
		return servicePagination.getSize(pSize);
	}

	private int getLastPage(int size, String search) {
		return serviceComputer.lastPage(size,search);
	}

	private int getPaginationAttribute(int currentPage, int lastPage) {
		return servicePagination.getPagination(lastPage, currentPage);
	}

	private List<DTOComputer> getComputersAttribute(Page<DTOComputer> page, OrderByEnum orderBy) throws PageNotFoundException{
		return serviceComputer.search(page.getIndex(), page.getLimit(), page.getSearch(), orderBy).getList();
	}

	private int getComputerCount(String search) {
		return serviceComputer.countByName(search);
	}

	private OrderByEnum getOrderByAttribute(String pOrderby) {
		OrderByEnum orderBy = OrderByEnum.DEFAULT;
		try{
			int indexOrderBy = Integer.parseInt(pOrderby);
			if(indexOrderBy>=0 && indexOrderBy<OrderByEnum.values().length) {
				orderBy = OrderByEnum.values()[indexOrderBy];
			}
		} catch(NumberFormatException e) {
			logger.warn("Cant parse {} to integer", pOrderby);
		} 
		return orderBy;
	}
}

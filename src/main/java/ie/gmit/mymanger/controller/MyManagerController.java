package ie.gmit.mymanger.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ie.gmit.mymanger.model.StatusInvoice;
import ie.gmit.mymanger.model.Invoice;
import ie.gmit.mymanger.repository.filter.TituloFilter;
import ie.gmit.mymanger.service.AddInvoiceService;


@Controller
@RequestMapping("/invoices")
public class MyManagerController {
	
	private static final String ADD_INVOICE = "AddInvoice";
	
	@Autowired
	private AddInvoiceService addInvoiceService;

	@RequestMapping("/newinvoice")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView(ADD_INVOICE);
		mv.addObject(new Invoice());
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Invoice invoice, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return ADD_INVOICE;
		}
		
		try {
			addInvoiceService.salvar(invoice);
			attributes.addFlashAttribute("mensagem", "Título salvo com sucesso!");
			return "redirect:/invoices/newinvoice";
		} catch (IllegalArgumentException e) {
			errors.rejectValue("dataVencimento", null, e.getMessage());
			return ADD_INVOICE;
		}
	}
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") TituloFilter filtro) {
		List<Invoice> todosTitulos = addInvoiceService.filtrar(filtro);
		
		ModelAndView mv = new ModelAndView("ListInvoice");
		mv.addObject("titulos", todosTitulos);
		return mv;
	}
	
	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Invoice invoice) {
		ModelAndView mv = new ModelAndView(ADD_INVOICE); 
		mv.addObject(invoice);
		return mv;
	}
	
	@RequestMapping(value="{codigo}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		addInvoiceService.excluir(codigo);
		
		attributes.addFlashAttribute("mensagem", "Título excluído com sucesso!");
		return "redirect:/invoices";
	}
	
	@RequestMapping(value = "/{codigo}/receber", method = RequestMethod.PUT)
	public @ResponseBody String receber(@PathVariable Long codigo) {
		return addInvoiceService.receber(codigo);
	}
	
	@ModelAttribute("todosStatusTitulo")
	public List<StatusInvoice> todosStatusTitulo() {
		return Arrays.asList(StatusInvoice.values());
	}
	
}

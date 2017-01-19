package ie.gmit.mymanger.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import ie.gmit.mymanger.model.StatusInvoice;
import ie.gmit.mymanger.model.Invoice;
import ie.gmit.mymanger.repository.Invoices;
import ie.gmit.mymanger.repository.filter.TituloFilter;

@Service
public class AddInvoiceService {

	@Autowired
	private Invoices invoices;
	
	public void salvar(Invoice invoice) {
		try {
			invoices.save(invoice);
		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato de data inválido");
		}
	}

	public void excluir(Long codigo) {
		invoices.delete(codigo);
	}

	public String receber(Long codigo) {
		Invoice invoice = invoices.findOne(codigo);
		invoice.setStatus(StatusInvoice.RECEBIDO);
		invoices.save(invoice);
		
		return StatusInvoice.RECEBIDO.getDescricao();
	}
	
	public List<Invoice> filtrar(TituloFilter filtro) {
		String descricao = filtro.getDescricao() == null ? "%" : filtro.getDescricao();
		return invoices.findByDescricaoContaining(descricao);
	}
	
}

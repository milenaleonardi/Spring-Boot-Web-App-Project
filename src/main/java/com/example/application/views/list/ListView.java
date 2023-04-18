
package com.example.application.views.list;

import com.example.application.data.entity.Contact;
import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;


@org.springframework.stereotype.Component
@Scope("prototype")
@PageTitle("Contacts | Vaadin CRM")
@Route(value = "", layout = MainLayout.class)
@PermitAll
public class ListView extends VerticalLayout {
    private final CrmService service;
    Grid<Contact> grid = new Grid<>(Contact.class); //Grid contendo os contatos
    TextField filterText = new TextField(); //campo de texto para inserir dados
    ContactForm form;

    public ListView(CrmService service) {
        this.service = service;
        addClassName("List-view"); //for css
        setSizeFull(); //ajusta-se ao tamanho do navegador

        configureGrid(); //adc método de configuração de grid
        configureForm();

        add(
                getToolBar(), //campo de filtros
                getContent()
        );

        updateList();
        closeEditor();
    }

    private void closeEditor() { //fechar a visualização do formulário
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    //buscar todos os contatos no repositório
    private void updateList() {
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }

    private Component getContent() {
        final HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureForm() {
        form = new ContactForm(service.findAllCompanies(), service.findAllStatuses());
        form.setWidth("25em");

        form.addSaveListener(this::saveContact);
        form.addDeleteListener(this::deleteContact);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveContact(ContactForm.SaveEvent event){
        service.saveContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        service.deleteContact(event.getContact());
        updateList();
        closeEditor();
    }

    private Component getToolBar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY); //optimização
        filterText.addValueChangeListener(e -> updateList());

        final Button addContactButton = new Button("Add contact");
        addContactButton.addClickListener(e -> addContact());

        final HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContent(new Contact()); //criar novo objeto Contato
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email"); //definindo colunas do grid
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company"); //definindo coluna costumizada
        grid.getColumns().forEach(col -> col.setAutoWidth(true)); //coluna proporcional

        grid.asSingleSelect().addValueChangeListener(e -> editContent(e.getValue()));

    }

    private void editContent(Contact contact) { //quando um valor for selecionado
        if(contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }

}

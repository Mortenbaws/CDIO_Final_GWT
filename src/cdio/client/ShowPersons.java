package cdio.client;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import cdio.client.service.ServiceClientImpl;
import cdio.shared.FieldVerifier;
import cdio.shared.UserDTO;

public class ShowPersons extends Composite {

	private FlexTable flex;
	private VerticalPanel vPanel;
	private String token;
	private ServiceClientImpl client;

	TextBox oprNavnTxt;
	TextBox iniTxt;
	TextBox cprTxt;
	TextBox passTxt;
	TextBox rolleTxt;
	SuggestBox suggestionBox;

	boolean oprNavnValid = true;
	boolean iniValid = true;
	boolean cprValid = true;
	boolean passValid = true;
	boolean rolleValid = true;

	int eventRowIndex;
	Anchor ok;
	Anchor previousCancel = null;

	public ShowPersons(ServiceClientImpl client, String token) {
		flex = new FlexTable();
		flex.setStyleName("FlexTable");
		flex.getRowFormatter().addStyleName(0, "FlexTable-Header");
		vPanel = new VerticalPanel();
		initWidget(vPanel);
		this.token = token;
		this.client = client;
		// suggest box to add rolle in flextabel
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		oracle.add("admin");
		oracle.add("farmaceut");
		oracle.add("vaerkfoerer");
		oracle.add("operatoer");

		client.service.getPersons(token, new AsyncCallback<List<UserDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(""+ caught.getMessage());
			}

			@Override
			public void onSuccess(List<UserDTO> result) {

				flex.setText(0, 0, "Operator ID");
				flex.setText(0, 1, "Navn");
				flex.setText(0, 2, "Initialer");
				flex.setText(0, 3, "CPR");
				flex.setText(0, 4, "Password");
				flex.setText(0, 5, "Rolle");

				for (int rowIndex = 0; rowIndex < result.size(); rowIndex++) {

					flex.setText(rowIndex + 1, 0, "" + result.get(rowIndex).getOprId());
					flex.setText(rowIndex + 1, 1, "" + result.get(rowIndex).getNavn());
					flex.setText(rowIndex + 1, 2, "" + result.get(rowIndex).getIni());
					flex.setText(rowIndex + 1, 3, "" + result.get(rowIndex).getCpr());
					flex.setText(rowIndex + 1, 4, "" + result.get(rowIndex).getPassword());
					flex.setText(rowIndex + 1, 5, "" + result.get(rowIndex).getRolle());

					flex.getCellFormatter().addStyleName(rowIndex + 1, 0, "FlexTable-Cell");
					flex.getCellFormatter().addStyleName(rowIndex + 1, 1, "FlexTable-Cell");
					flex.getCellFormatter().addStyleName(rowIndex + 1, 2, "FlexTable-Cell");
					flex.getCellFormatter().addStyleName(rowIndex + 1, 3, "FlexTable-Cell");
					flex.getCellFormatter().addStyleName(rowIndex + 1, 4, "FlexTable-Cell");
					flex.getCellFormatter().addStyleName(rowIndex + 1, 5, "FlexTable-Cell");

					Anchor edit = new Anchor("edit");
					flex.setWidget(rowIndex + 1, 6, edit);

					edit.addClickHandler(new EditHandler());
				}

				// flex.setStyleName("FlexTable");

			}

		});
		vPanel.add(flex);
		// create textboxs
		oprNavnTxt = new TextBox();
		oprNavnTxt.setWidth("80px");
		oprNavnTxt.setStyleName("TextBox-style");

		iniTxt = new TextBox();
		iniTxt.setWidth("40px");
		iniTxt.setStyleName("TextBox-style");

		cprTxt = new TextBox();
		cprTxt.setWidth("80px");
		cprTxt.setStyleName("TextBox-style");

		passTxt = new TextBox();
		passTxt.setWidth("80px");
		passTxt.setStyleName("TextBox-style");

		rolleTxt = new TextBox();
		rolleTxt.setWidth("80px");
		rolleTxt.setStyleName("TextBox-style");

		suggestionBox = new SuggestBox(oracle);
		suggestionBox.setWidth("80px");

	}

	private class EditHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			if (previousCancel != null)
				previousCancel.fireEvent(new ClickEvent() {
				});

			eventRowIndex = flex.getCellForEvent(event).getRowIndex();

			oprNavnTxt.setText(flex.getText(eventRowIndex, 1));
			iniTxt.setText(flex.getText(eventRowIndex, 2));
			cprTxt.setText(flex.getText(eventRowIndex, 3));
			passTxt.setText(flex.getText(eventRowIndex, 4));
			suggestionBox.setText(flex.getText(eventRowIndex, 5));

			flex.setWidget(eventRowIndex, 1, oprNavnTxt);
			flex.setWidget(eventRowIndex, 2, iniTxt);
			flex.setWidget(eventRowIndex, 3, cprTxt);
			flex.setWidget(eventRowIndex, 4, passTxt);
			flex.setWidget(eventRowIndex, 5, suggestionBox);

			oprNavnTxt.setFocus(true);

			final Anchor edit = (Anchor) event.getSource();

			final String oprNavn = oprNavnTxt.getText();
			final String ini = iniTxt.getText();
			final String cpr = cprTxt.getText();
			final String pass = passTxt.getText();
			final String rolle = suggestionBox.getText();

			ok = new Anchor("ok");
			ok.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					flex.setText(eventRowIndex, 1, oprNavnTxt.getText());
					flex.setText(eventRowIndex, 2, iniTxt.getText());
					flex.setText(eventRowIndex, 3, cprTxt.getText());
					flex.setText(eventRowIndex, 4, passTxt.getText());
					flex.setText(eventRowIndex, 5, suggestionBox.getText());

					UserDTO User = new UserDTO(Integer.parseInt(flex.getText(eventRowIndex, 0)), oprNavnTxt.getText(),
							iniTxt.getText(), cprTxt.getText(), passTxt.getText(), suggestionBox.getText());

					client.service.updateUser(ShowPersons.this.token, User, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(""+caught.getMessage());

						}

						@Override
						public void onSuccess(Void result) {
							// TODO Auto-generated method stub

						}

					});
					flex.setWidget(eventRowIndex, 6, edit);
					flex.clearCell(eventRowIndex, 7);
					if (!(rolle.equals("operatoer") || rolle.equals("admin"))) {
						flex.clearCell(eventRowIndex, 8);
						previousCancel = null;
					}
				}
			});

			Anchor cancel = new Anchor("cancel");
			previousCancel = cancel;
			cancel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					oprNavnTxt.setText(oprNavn);
					oprNavnTxt.fireEvent(new KeyUpEvent() {
					});

					iniTxt.setText(ini);
					iniTxt.fireEvent(new KeyUpEvent() {
					}); // validation

					cprTxt.setText(cpr);
					cprTxt.fireEvent(new KeyUpEvent() {
					}); // validation

					passTxt.setText(pass);
					passTxt.fireEvent(new KeyUpEvent() {
					}); // validation

					suggestionBox.setText(rolle);
					suggestionBox.fireEvent(new KeyUpEvent() {
					}); // validation

					flex.setText(eventRowIndex, 1, oprNavn);
					flex.setText(eventRowIndex, 2, ini);
					flex.setText(eventRowIndex, 3, cpr);
					flex.setText(eventRowIndex, 4, pass);
					flex.setText(eventRowIndex, 5, rolle);
					// restore edit link
					flex.setWidget(eventRowIndex, 6, edit);
					flex.clearCell(eventRowIndex, 7);
					if (!(rolle.equals("operatoer") || rolle.equals("admin"))) {
						flex.clearCell(eventRowIndex, 8);
					}
					previousCancel = null;
				}

			});

			Anchor delete = new Anchor("delete");
			delete.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					int opr_id = Integer.parseInt(flex.getText(flex.getCellForEvent(event).getRowIndex(), 0));
					client.service.deleteUser(token, opr_id, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(""+caught.getMessage());

						}

						@Override
						public void onSuccess(Void result) {

							flex.setText(eventRowIndex, 1, oprNavn);
							flex.setText(eventRowIndex, 2, ini);
							flex.setText(eventRowIndex, 3, cpr);
							flex.setText(eventRowIndex, 4, pass);
							flex.setText(eventRowIndex, 5, rolle);

							flex.removeRow(eventRowIndex);

							// restore edit link
							flex.setWidget(eventRowIndex, 6, edit);
							flex.clearCell(eventRowIndex, 7);

							if (!(rolle.equals("operatoer") || rolle.equals("admin"))) {
								flex.clearCell(eventRowIndex, 8);
							}
							previousCancel = null;

						}

					});

				}

			});

			oprNavnTxt.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {

					if (!FieldVerifier.isValidName(oprNavnTxt.getText())) {
						oprNavnTxt.setStyleName("gwt-TextBox-invalidEntry");
						oprNavnValid = false;
					} else {
						oprNavnTxt.setStyleName("TextBox-style");
						oprNavnValid = true;
					}
					checkFormValid();
					;
				}

			});

			iniTxt.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {

					if (!FieldVerifier.isValidName(iniTxt.getText())) {
						iniTxt.setStyleName("gwt-TextBox-invalidEntry");
						iniValid = false;
					} else {
						iniTxt.setStyleName("TextBox-style");
						iniValid = true;
					}
					checkFormValid();
					;
				}

			});

			cprTxt.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {

					if (!FieldVerifier.isValidCpr(cprTxt.getText())) {
						cprTxt.setStyleName("gwt-TextBox-invalidEntry");
						cprValid = false;
					} else {
						cprTxt.setStyleName("TextBox-style");
						cprValid = true;
					}
					checkFormValid();
					;
				}

			});

			passTxt.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (!FieldVerifier.isVaildPassword(passTxt.getText())) {
						passTxt.setStyleName("gwt-TextBox-invalidEntry");
						passValid = false;
					} else {
						passTxt.setStyleName("TextBox-style");
						passValid = true;
					}
					checkFormValid();
					;
				}

			});

			suggestionBox.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (!FieldVerifier.isValidRolleName(suggestionBox.getText())) {
						suggestionBox.setStyleName("gwt-TextBox-invalidEntry");
						rolleValid = false;
					} else {
						suggestionBox.setStyleName("TextBox-style");
						rolleValid = true;
					}
					checkFormValid();
					;
				}

			});

			flex.setWidget(eventRowIndex, 6, ok);
			flex.setWidget(eventRowIndex, 7, cancel);
			// final int lort = ;

			// System.out.println("pleb------"+flex.getText(eventRowIndex, 5));
			if (!(rolle.equals("operatoer") || rolle.equals("admin"))) {
				flex.setWidget(eventRowIndex, 8, delete);
			}
		}
	}

	private void checkFormValid() {
		if (oprNavnValid && iniValid && cprValid && passValid && rolleValid)

			flex.setWidget(eventRowIndex, 6, ok);

		else
			flex.setText(eventRowIndex, 6, "ok");

	}

}
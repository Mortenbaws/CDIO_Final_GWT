package cdio.server.DAOImpl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cdio.server.Connector;
import cdio.server.DAOinterfaces.DALException;
import cdio.server.DAOinterfaces.RaavareDAO;
import cdio.shared.RaavareDTO;

public class MYSQLRaavareDAO implements RaavareDAO {

	@Override
	public RaavareDTO getRaavare(int raavareId) throws DALException {
		try {
			CallableStatement getraavare = Connector.getInstance().getConnection()
					.prepareCall("call get_raavare(?)");
			getraavare.setInt(1, raavareId);
			ResultSet rs = getraavare.executeQuery();
			if (rs.first()) {
				String raavare_navn = rs.getString(2);
				String leverandoer = rs.getString(3);
				RaavareDTO newRec = new RaavareDTO(raavareId, raavare_navn, leverandoer);
				return newRec;
			}
		} catch (SQLException e) {
			throw new DALException(e);
		}
		return null;
	}

	@Override
	public List<RaavareDTO> getRaavareList() throws DALException {
		List<RaavareDTO> list = new ArrayList<RaavareDTO>();
		try {
			ResultSet rs = Connector.getInstance().doQuery("SELECT * FROM raavare");
			while (rs.next()) {
				RaavareDTO current = new RaavareDTO(rs.getInt(1), rs.getString(2), rs.getString(3));
				list.add(current);
			}
		} catch (SQLException e) {
			throw new DALException(e);
		}
		return list;
	}

	@Override
	public void createRaavare(RaavareDTO raavare) throws DALException {
		try {

			Connector.getInstance().doQuery("SELECT add_raavare(" + raavare.getRaavareId() + ",'"
					+ raavare.getRaavareNavn() + "','" + raavare.getLeverandoer() + "')");

		} catch (Exception e) {
			System.err.println("Could not create Raavare, check if the database is running!");
			e.printStackTrace();
		}
	}

	@Override
	public void updateRaavare(RaavareDTO raavare) throws DALException {
		try {
			Connector.getInstance().doQuery("SELECT update_raavare(" + raavare.getRaavareId() + ",'"
					+ raavare.getRaavareNavn() + "','" + raavare.getLeverandoer() + "')");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
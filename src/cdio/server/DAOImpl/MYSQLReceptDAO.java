package cdio.server.DAOImpl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cdio.server.Connector;
import cdio.server.DAOinterfaces.DALException;
import cdio.server.DAOinterfaces.ReceptDAO;
import cdio.shared.ReceptDTO;

public class MYSQLReceptDAO implements ReceptDAO {

	@Override
	public ReceptDTO getRecept(int receptId) throws DALException {
		try {
			CallableStatement getRecept = Connector.getInstance().getConnection()
					.prepareCall("call get_recept(?)");
			getRecept.setInt(1, receptId);
			ResultSet rs = getRecept.executeQuery();
			if (rs.first()) {
				String recept_navn = rs.getString(2);
				ReceptDTO newRec = new ReceptDTO(receptId, recept_navn);
				newRec.setReceptId(receptId);
				return newRec;
			}
		} catch (SQLException e) {
			throw new DALException(e);
		}
		return null;
	}

	@Override
	public List<ReceptDTO> getReceptList() throws DALException {
		List<ReceptDTO> list = new ArrayList<ReceptDTO>();
		try {
			ResultSet rs = Connector.getInstance().doQuery("SELECT * FROM recept");
			while (rs.next()) {
				ReceptDTO current = new ReceptDTO(rs.getInt(1), rs.getString(2));
				current.setReceptId(rs.getInt(1));
				list.add(current);
			}
		} catch (SQLException e) {
			throw new DALException(e);
		}
		return list;
	}

	@Override
	public void createRecept(ReceptDTO recept) throws DALException {
		try {
			int id = 0;
			CallableStatement createRecept = Connector.getInstance().getConnection()
					.prepareCall("call add_recept(?,?)");
			createRecept.setString(2, recept.getReceptNavn());
			createRecept.setString(1, "" + recept.getReceptId());

			createRecept.execute();

			recept.setReceptId(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not create recept, check if the database is running!");
		}
	}

	@Override
	public void updateRecept(ReceptDTO recept) throws DALException {
		try {
			Connector.getInstance().doUpdate("UPDATE recept SET  receptNavn= '" + recept.getReceptNavn()
					+ "' WHERE receptId = " + recept.getReceptId());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ResultSet getFullRecept(String navn) {
		try {
			ResultSet getFullRecept = Connector.getInstance()
					.doQuery("select * from view_fuldrecept where receptNavn = '" + navn + "';");
			if (getFullRecept.next()) {
				System.out.println(" get fuldrecept er ....... " + getFullRecept.getString(1));
				return getFullRecept;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResultSet checkRecept(String navn) {

		try {
			ResultSet getFullRecept = Connector.getInstance()
					.doQuery("select * from recept where receptNavn = '" + navn + "';");
			if (getFullRecept.next()) {

				return getFullRecept;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method checks if a recept_id is already stored in the database. In
	 * case of it exists in database, the method will return true, and false in
	 * other case;
	 * 
	 * @param receptID
	 * @return true or false
	 */
	public boolean receptIdExists(int recept_ID) {
		try {
			ResultSet rs = Connector.getInstance().doQuery("Select tjek_receptID(" + recept_ID + ")");
			int result = rs.getInt(1);
			if (result == 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

}

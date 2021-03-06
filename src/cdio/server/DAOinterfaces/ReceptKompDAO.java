package cdio.server.DAOinterfaces;

import java.util.List;

import cdio.shared.ReceptKompDTO;

public interface ReceptKompDAO {
	ReceptKompDTO getReceptKomp(int receptId, int raavareId) throws DALException;

	List<ReceptKompDTO> getReceptKompList(int receptId) throws DALException;

	List<ReceptKompDTO> getReceptKompList() throws DALException;

	void createReceptKomp(ReceptKompDTO receptkomponent) throws DALException;

	void updateReceptKomp(ReceptKompDTO receptkomponent) throws DALException;

	List<String> getReceptRaavarer(int id);

	int getRaavareIdFromName(String name);
}

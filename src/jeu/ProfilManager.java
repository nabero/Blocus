package jeu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

public class ProfilManager {
	
    private static final String PROFIL_FILE = "data/pompompom.json";
    private Profil profil;
    
    /**
     * Retrieves the player's profile, creating one if needed.
     */
    public Profil retrieveProfile()
    {
        FileHandle profileDataFile = Gdx.files.local(PROFIL_FILE);

		// if the profile is already loaded, just return it
		if (profil != null)
			return profil;

		// load the profile from the data file
		try {
			if (profileDataFile.exists()) {
				Json json = new Json();
				// read the file as text
				String profileAsText = profileDataFile.readString().trim();
				// decode the contents (if it's base64 encoded)
				if (profileAsText.matches("^[A-Za-z0-9/+=]+$")) 
					profileAsText = Base64Coder.decodeString(profileAsText);
				
				// restore the state
				profil = json.fromJson(Profil.class, profileAsText);
			} else {
				// create a new profile data file
				profil = new Profil();
				persist(profil);
			}
		} catch (Exception e) {
			// recover by creating a fresh new profile data file;
			profil = new Profil();
			persist(profil);
		}
		// return the result
		return profil;
    }

    /**
     * Persists the given profile.
     */
	protected void persist(Profil profil) {
		// create the handle for the profile data file
		FileHandle profileDataFile = Gdx.files.local(PROFIL_FILE);
		// create the JSON utility object
		Json json = new Json();
		// convert the given profile to text
		String profileAsText;
		profileAsText = json.toJson(profil);

		// encode the text
		profileAsText = Base64Coder.encodeString(profileAsText);
		// write the profile data file
		profileDataFile.writeString(profileAsText, false);
	}

    /**
     * Persists the player's profile.
     * If no profile is available, this method does nothing.
     */
	public void persist() {
		if (profil != null) {
			persist(profil);
		}
	}
}

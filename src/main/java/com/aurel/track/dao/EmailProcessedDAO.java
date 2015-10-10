/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

package com.aurel.track.dao;

import java.util.List;

import com.aurel.track.beans.TEmailProcessedBean;

public interface EmailProcessedDAO {
    /* Gets a mails processed from the emailProcessed table
       * @param objectID
       * @return
       */
      TEmailProcessedBean loadByPrimaryKey(Integer objectID);

      /**
       * Loads all templates from TEmailProcessed table
       * @return
       */
      List<TEmailProcessedBean> loadAll();

      /**
       * Save  emailProcessed in the TExportTemplate table
       * @param emailProcessed
       * @return
       */
      Integer save(TEmailProcessedBean emailProcessed);


      /**
       * Deletes a exportTemplate from the TEmailProcessed table
       * Is deletable should return true before calling this method
       * @param objectID
       */
      void delete(Integer objectID);


      /**
       * Verifies whether a screenField is deletable
       * @param objectID
       */
      boolean isDeletable(Integer objectID);

}

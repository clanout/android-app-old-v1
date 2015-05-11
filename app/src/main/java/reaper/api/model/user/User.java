package reaper.api.model.user;

import reaper.api.model.core.Model;

/**
 * Created by Aditya on 06-04-2015.
 */
public class User implements Model
{
    private String id;
    private String name;
    private boolean isBlocked;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isBlocked()
    {
        return isBlocked;
    }

    public void setBlocked(boolean isBlocked)
    {
        this.isBlocked = isBlocked;
    }


    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }

        if (!(o instanceof User))
        {
            return false;
        }
        else
        {
            User other = (User) o;
            if (id.equals(other.id))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

}

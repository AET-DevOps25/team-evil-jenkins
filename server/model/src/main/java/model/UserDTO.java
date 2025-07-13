// In your shared 'model' module
package model;

import java.util.List;

public record UserDTO(String id, String name, List<String> sportInterests) 
{
}
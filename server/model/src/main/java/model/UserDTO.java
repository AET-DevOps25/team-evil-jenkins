// In your shared 'model' module
package model;

import java.util.List;
import java.util.Map;

public record UserDTO(String id,
                        String name,
                        String picture,
                        String bio,
                        String skillLevel,
                        Map<String, List<String>> availability,
                        List<String> sportInterests) 
{
}
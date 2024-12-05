package social.network.microservice_friend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendDtoList {
   List<RecommendDto> recommendDtoList=new ArrayList<>();
}

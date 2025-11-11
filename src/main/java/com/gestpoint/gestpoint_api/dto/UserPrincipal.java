/**
 * @author falvesmac
 */

package com.gestpoint.gestpoint_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserPrincipal {
    private String email;
    private Long tenantId;
}
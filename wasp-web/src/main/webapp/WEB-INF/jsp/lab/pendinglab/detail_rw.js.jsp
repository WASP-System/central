<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		$("#billing_institution").keyup(function() {
			getInstituteNames();
		});
	});

	function getInstituteNames() {
		if ($("#billing_institution").val().length == 1) {
			$.getJSON("<wasp:relativeUrl value='autocomplete/getInstitutesForDisplay.do' />", {
				instituteNameFragment : $("#billing_institution").val()
			}, function(data) {
				$("input#billing_institution").autocomplete(data);
			});
		}
	}
</script>
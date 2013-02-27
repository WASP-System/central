

<script type="text/javascript">
	$(document).ready(function() {
		$("#billing_institution").keyup(function() {
			getInstituteNames();
		});
	});

	function getInstituteNames() {
		if ($("#billing_institution").val().length == 1) {
			$.getJSON("/wasp/autocomplete/getInstitutesForDisplay.do", {
				instituteNameFragment : $("#billing_institution").val()
			}, function(data) {
				$("input#billing_institution").autocomplete(data);
			});
		}
	}
</script>